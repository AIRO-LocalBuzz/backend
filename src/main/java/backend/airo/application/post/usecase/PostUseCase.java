package backend.airo.application.post.usecase;

import backend.airo.domain.post.command.CreatePostCommand;
import backend.airo.domain.post.command.DeletePostCommand;
import backend.airo.domain.post.command.UpdatePostCommand;
import backend.airo.domain.post.dto.PostSearchCriteria;
import backend.airo.domain.post.query.GetPostQuery;
import backend.airo.domain.post.query.GetPostListQuery;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.repository.PostRepository;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.events.PostPublishedEvent;
import backend.airo.domain.post.events.PostStatusChangedEvent;
import backend.airo.domain.post.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * PostUseCase - 게시물 관련 비즈니스 로직 처리
 * Application Layer의 핵심 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostUseCase {

    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;

    // ===== 게시물 생성 =====

    /**
     * 게시물 생성
     */
    @Transactional
    public Post createPost(CreatePostCommand command) {
        log.info("게시물 생성 시작: title={}, userId={}, status={}",
                command.title(), command.userId(), command.status());

        // 발행 조건 검증
        if (command.status() == PostStatus.PUBLISHED && !command.canPublish()) {
            throw new PostPublishException(null, "발행에 필요한 필수 정보가 누락되었습니다 (카테고리, 위치)");
        }

        // 도메인 객체 생성
        Post post = createPostFromCommand(command);

        // 게시물 저장
        Post savedPost = postRepository.save(post);
        log.info("게시물 저장 완료: id={}, title={}", savedPost.getId(), savedPost.getTitle());

        // 이미지 연결 처리
        if (command.hasImages()) {
            processPostImages(savedPost.getId(), command.imageIds());
        }

        // 태그 연결 처리
        if (command.hasTags()) {
            processPostTags(savedPost.getId(), command.tags());
        }

        // 발행 이벤트 발행
        if (savedPost.getStatus() == PostStatus.PUBLISHED) {
            publishPostPublishedEvent(savedPost, command.userId());
        }

        return savedPost;
    }

    // ===== 게시물 조회 =====

    /**
     * 게시물 단건 조회
     */
    public Post getPostById(GetPostQuery query) {
        log.debug("게시물 조회: id={}, requesterId={}", query.postId(), query.requesterId());

        Post post;

        // 연관 데이터 포함 조회
        if (query.hasIncludes()) {
            Optional<Post> optionalPost = postRepository.findByIdWithAssociations(
                    query.postId(),
                    query.includeImages(),
                    query.includeTags(),
                    query.includeComments()
            );
            post = optionalPost.orElseGet(() -> postRepository.findById(query.postId()));
        } else {
            post = postRepository.findById(query.postId());
        }

        // 접근 권한 검증
        validatePostAccess(post, query.requesterId());

        // 조회수 증가
        if (query.shouldIncrementViewCount() && query.requesterId() != null) {
            incrementViewCount(query.postId());
        }

        return post;
    }

    /**
     * 게시물 목록 조회
     */
    public Page<Post> getPostList(GetPostListQuery query) {
        log.debug("게시물 목록 조회: page={}, size={}, userId={}",
                query.page(), query.size(), query.userId());

        Pageable pageable = createPageable(query);

        // 사용자별 조회를 우선 순위로 변경
        if (query.userId() != null) {
            return postRepository.findByUserId(query.userId(), pageable);
        } else if (query.hasKeyword()) {
            return searchPostsByKeyword(query, pageable);
        } else if (query.hasFilters()) {
            return searchPostsByCriteria(query, pageable);
        } else {
            return postRepository.findPublishedPosts(pageable);
        }
    }

    // ===== 게시물 수정 =====

    /**
     * 게시물 수정
     */
    @Transactional
    public Post updatePost(UpdatePostCommand command) {
        log.info("게시물 수정 시작: id={}, requesterId={}", command.postId(), command.requesterId());

        // 기존 게시물 조회
        Post existingPost = postRepository.findById(command.postId());

        // 권한 검증
        validatePostOwnership(existingPost, command.requesterId());

        // 수정 사항이 없으면 기존 게시물 반환
        if (!command.hasChanges()) {
            return existingPost;
        }

        // 상태 변경 검증을 업데이트 전에 수행
        if (command.isStatusChange()) {
            validateStatusChange(existingPost, command.status());
        }

        // 게시물 업데이트
        Post updatedPost = updatePostFromCommand(existingPost, command);

        // 저장
        Post savedPost = postRepository.save(updatedPost);

        // 상태 변경 이벤트 발행
        if (command.isStatusChange()) {
            publishStatusChangedEvent(existingPost, savedPost, command.requesterId(), command.changeReason());
        }

        log.info("게시물 수정 완료: id={}", savedPost.getId());
        return savedPost;
    }
    // ===== 게시물 삭제 =====

    /**
     * 게시물 삭제
     */
    @Transactional
    public void deletePost(DeletePostCommand command) {
        log.info("게시물 삭제 시작: id={}, requesterId={}, force={}",
                command.postId(), command.requesterId(), command.isForceDelete());

        // 기존 게시물 조회
        Post existingPost = postRepository.findById(command.postId());

        // 권한 검증
        validatePostOwnership(existingPost, command.requesterId());

        // 삭제 가능 여부 검증
        if (!command.isForceDelete()) {
            validatePostDeletion(existingPost);
        }

        // 게시물 삭제
        postRepository.deleteById(command.postId());

        log.info("게시물 삭제 완료: id={}", command.postId());
    }

    // ===== 게시물 좋아요 =====

    /**
     * 게시물 좋아요/취소
     */
    @Transactional
    public boolean likePost(Long postId, Long userId) {
        log.debug("게시물 좋아요 처리: postId={}, userId={}", postId, userId);

        // 게시물 존재 확인
        Post post = postRepository.findById(postId);

        // 좋아요 상태 확인 및 토글 처리
        boolean isCurrentlyLiked = checkIfUserLikedPost(postId, userId);

        if (isCurrentlyLiked) {
            // 좋아요 취소
            removeLike(postId, userId);
            postRepository.updateLikeCount(postId, post.getLikeCount() - 1);
            return false;
        } else {
            // 좋아요 추가
            addLike(postId, userId);
            postRepository.updateLikeCount(postId, post.getLikeCount() + 1);
            return true;
        }
    }

    // ===== 통계 및 집계 =====

    /**
     * 사용자별 게시물 통계 조회
     */
    public PostUserStats getUserPostStats(Long userId) {
        long totalCount = postRepository.countByUserId(userId);
        long publishedCount = postRepository.countByUserIdAndStatus(userId, PostStatus.PUBLISHED);
        long draftCount = postRepository.countByUserIdAndStatus(userId, PostStatus.DRAFT);
        long archivedCount = postRepository.countByUserIdAndStatus(userId, PostStatus.ARCHIVED);

        return new PostUserStats(userId, totalCount, publishedCount, draftCount, archivedCount);
    }

    /**
     * 인기 게시물 목록 조회
     */
    public List<Post> getPopularPosts(int limit) {
        return postRepository.findPopularPosts(limit);
    }

    /**
     * 최근 인기 게시물 목록 조회
     */
    public List<Post> getRecentPopularPosts(int days, int limit) {
        return postRepository.findRecentPopularPosts(days, limit);
    }

    /**
     * 좋아요 많은 게시물 목록 조회
     */
    public List<Post> getMostLikedPosts(int limit) {
        return postRepository.findMostLikedPosts(limit);
    }

    // ===== 배치 처리 =====

    /**
     * 여러 게시물 상태 일괄 변경
     */
    @Transactional
    public int updatePostStatusBatch(List<Long> postIds, PostStatus status) {
        log.info("게시물 상태 일괄 변경: {} 건, 상태={}", postIds.size(), status);
        return postRepository.updateStatusBatch(postIds, status);
    }

    /**
     * 오래된 임시저장 게시물 정리
     */
    @Transactional
    public int cleanupOldDraftPosts(int days) {
        log.info("오래된 임시저장 게시물 정리: {} 일 이전", days);
        return postRepository.deleteOldDraftPosts(days);
    }

    /**
     * 비활성 사용자 게시물 보관
     */
    @Transactional
    public int archiveInactiveUserPosts(List<Long> userIds) {
        log.info("비활성 사용자 게시물 보관: {} 명", userIds.size());
        return postRepository.archiveInactiveUserPosts(userIds);
    }

    // ===== Private Helper Methods =====

    /**
     * Command로부터 Post 도메인 객체 생성
     */
    private Post createPostFromCommand(CreatePostCommand command) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime publishedAt = (command.status() == PostStatus.PUBLISHED) ? now : null;

        return new Post(
                null, // ID는 저장 시 생성
                command.userId(),
                command.categoryId(),
                command.locationId(),
                command.title(),
                command.content(),
                null, // summary는 나중에 AI로 생성
                command.status(),
                command.travelDate(),
                0, // 초기 조회수
                0, // 초기 좋아요 수
                0, // 초기 댓글 수
                command.isFeatured(),
                publishedAt
        );
    }

    /**
     * Command로부터 Post 업데이트
     */
    private Post updatePostFromCommand(Post existingPost, UpdatePostCommand command) {
        return new Post(
                existingPost.getId(),
                existingPost.getUserId(),
                command.categoryId() != null ? command.categoryId() : existingPost.getCategoryId(),
                command.locationId() != null ? command.locationId() : existingPost.getLocationId(),
                command.title() != null ? command.title() : existingPost.getTitle(),
                command.content() != null ? command.content() : existingPost.getContent(),
                existingPost.getSummary(),
                command.status() != null ? command.status() : existingPost.getStatus(),
                command.travelDate() != null ? command.travelDate() : existingPost.getTravelDate(),
                existingPost.getViewCount(),
                existingPost.getLikeCount(),
                existingPost.getCommentCount(),
                command.isFeatured() != null ? command.isFeatured() : existingPost.getIsFeatured(),
                command.status() == PostStatus.PUBLISHED && existingPost.getPublishedAt() == null
                        ? LocalDateTime.now() : existingPost.getPublishedAt()
        );
    }

    /**
     * 페이징 객체 생성
     */
    private Pageable createPageable(GetPostListQuery query) {
        Sort sort = Sort.by(
                query.isDescending() ? Sort.Direction.DESC : Sort.Direction.ASC,
                query.sortBy()
        );
        return PageRequest.of(query.page(), query.size(), sort);
    }

    /**
     * 키워드로 게시물 검색
     */
    private Page<Post> searchPostsByKeyword(GetPostListQuery query, Pageable pageable) {
        return switch (query.searchScope()) {
            case "title" -> postRepository.findByTitleContaining(query.keyword(), pageable);
            case "content" -> postRepository.findByContentContaining(query.keyword(), pageable);
            default -> postRepository.searchFullText(query.keyword(), pageable);
        };
    }

    /**
     * 복합 조건으로 게시물 검색
     */
    private Page<Post> searchPostsByCriteria(GetPostListQuery query, Pageable pageable) {
        PostSearchCriteria criteria = new PostSearchCriteria(
                query.keyword(),
                query.searchScope(),
                query.statuses(),
                query.userId(),
                query.categoryId(),
                query.locationId(),
                query.tags(),
                query.isFeatured(),
                query.startDate(),
                query.endDate(),
                null, null, null, null, null
        );
        return postRepository.findByCriteria(criteria, pageable);
    }

    /**
     * 게시물 접근 권한 검증
     */
    private void validatePostAccess(Post post, Long requesterId) {
        if (post.getStatus() != PostStatus.PUBLISHED && !isPostOwner(post, requesterId)) {
            throw new PostAccessDeniedException(post.getId(), requesterId);
        }
    }

    /**
     * 게시물 소유권 검증
     */
    private void validatePostOwnership(Post post, Long requesterId) {
        if (!isPostOwner(post, requesterId)) {
            throw new PostAccessDeniedException(post.getId(), requesterId);
        }
    }

    /**
     * 게시물 소유자 확인
     */
    private boolean isPostOwner(Post post, Long userId) {
        return userId != null && userId.equals(post.getUserId());
    }

    /**
     * 상태 변경 가능 여부 검증
     */
    private void validateStatusChange(Post post, PostStatus newStatus) {
        if (!isValidStatusTransition(post.getStatus(), newStatus)) {
            throw new PostStatusChangeException(post.getId(), post.getStatus(), newStatus);
        }

        // 발행 조건 검증
        if (newStatus == PostStatus.PUBLISHED && !canPublishPost(post)) {
            throw new PostPublishException(post.getId(), "발행 조건을 만족하지 않습니다");
        }
    }



    /**
     * 유효한 상태 전환인지 확인
     */
    private boolean isValidStatusTransition(PostStatus currentStatus, PostStatus newStatus) {
        return switch (currentStatus) {
            case DRAFT -> newStatus == PostStatus.PUBLISHED;
            case PUBLISHED -> newStatus == PostStatus.ARCHIVED || newStatus == PostStatus.DRAFT;
            case ARCHIVED -> newStatus == PostStatus.PUBLISHED || newStatus == PostStatus.DRAFT;
        };
    }

    /**
     * 게시물 발행 가능 여부 확인
     */
    private boolean canPublishPost(Post post) {
        // 필수 필드 검증
        if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
            return false;
        }
        if (post.getContent() == null || post.getContent().trim().isEmpty()) {
            return false;
        }

        // TODO: Post 도메인에 categoryId, locationId 필드 추가 후 활성화
        if (post.getCategoryId() == null) {
            return false;
        }
        if (post.getLocationId() == null) {
            return false;
        }

        return true;
    }

    /**
     * 게시물 삭제 가능 여부 검증
     */
    private void validatePostDeletion(Post post) {
        if (post.getStatus() == PostStatus.PUBLISHED) {
            throw new PostDeleteException(post.getId(), "발행된 게시물은 삭제할 수 없습니다");
        }
    }

    /**
     * 조회수 증가
     */
    private void incrementViewCount(Long postId) {
        try {
            postRepository.incrementViewCount(postId, 1);
        } catch (Exception e) {
            log.warn("조회수 증가 실패: postId={}", postId, e);
        }
    }

    /**
     * 이미지 연결 처리
     */
    private void processPostImages(Long postId, List<Long> imageIds) {
        // 실제 구현에서는 ImageService 등을 통해 처리
        log.debug("게시물 이미지 연결: postId={}, imageCount={}", postId, imageIds.size());
    }

    /**
     * 태그 연결 처리
     */
    private void processPostTags(Long postId, List<String> tags) {
        // 실제 구현에서는 TagService 등을 통해 처리
        log.debug("게시물 태그 연결: postId={}, tagCount={}", postId, tags.size());
    }

    /**
     * 사용자 좋아요 상태 확인
     */
    private boolean checkIfUserLikedPost(Long postId, Long userId) {
        // 실제 구현에서는 PostLikeRepository 등을 통해 처리
        return false;
    }

    /**
     * 좋아요 추가
     */
    private void addLike(Long postId, Long userId) {
        // 실제 구현에서는 PostLikeRepository 등을 통해 처리
        log.debug("좋아요 추가: postId={}, userId={}", postId, userId);
    }

    /**
     * 좋아요 제거
     */
    private void removeLike(Long postId, Long userId) {
        // 실제 구현에서는 PostLikeRepository 등을 통해 처리
        log.debug("좋아요 제거: postId={}, userId={}", postId, userId);
    }

    /**
     * 게시물 발행 이벤트 발행
     */
    private void publishPostPublishedEvent(Post post, Long userId) {
        try {
            PostPublishedEvent event = PostPublishedEvent.of(
                    post.getId(), userId, post.getTitle(), PostStatus.DRAFT, post.getPublishedAt()
            );
            eventPublisher.publishEvent(event);
            log.debug("게시물 발행 이벤트 발행: postId={}", post.getId());
        } catch (Exception e) {
            log.error("게시물 발행 이벤트 발행 실패: postId={}", post.getId(), e);
        }
    }

    /**
     * 상태 변경 이벤트 발행
     */
    private void publishStatusChangedEvent(Post oldPost, Post newPost, Long userId, String reason) {
        try {
            PostStatusChangedEvent event = PostStatusChangedEvent.of(
                    newPost.getId(), userId, newPost.getTitle(),
                    oldPost.getStatus(), newPost.getStatus(), reason
            );
            eventPublisher.publishEvent(event);
            log.debug("게시물 상태 변경 이벤트 발행: postId={}, status={}→{}",
                    newPost.getId(), oldPost.getStatus(), newPost.getStatus());
        } catch (Exception e) {
            log.error("게시물 상태 변경 이벤트 발행 실패: postId={}", newPost.getId(), e);
        }
    }

    /**
     * 사용자별 게시물 통계 DTO
     */
    public record PostUserStats(
            Long userId,
            long totalCount,
            long publishedCount,
            long draftCount,
            long archivedCount
    ) {}
}