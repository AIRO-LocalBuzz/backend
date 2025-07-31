package backend.airo.domain.post.command;

import backend.airo.api.post.dto.PostUpdateRequest;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.exception.PostPublishException;
import backend.airo.domain.post.exception.PostStatusChangeException;
import backend.airo.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static backend.airo.domain.post.exception.PostErrorCode.POST_CANNOT_CHANGE_STATUS;
import static backend.airo.domain.post.exception.PostErrorCode.POST_PUBLISH_INVALID_CONDITION;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpdatePostCommandService{

    private final PostRepository postRepository;

    public Post handle(PostUpdateRequest request, Post existingPost){

        // 수정 사항이 없으면 기존 게시물 반환
        if (!request.hasChanges()) {
            return existingPost;
        }

        // 상태 변경 검증을 업데이트 전에 수행
        if (request.isStatusChange()) {
            validateStatusChange(existingPost, request.status());
        }

        // 게시물 업데이트
        Post updatedPost = updatePostFromCommand(existingPost, request);

        // 저장
        Post savedPost = postRepository.save(updatedPost);


        log.info("게시물 수정 완료: id={}", savedPost.getId());
        return savedPost;
    }


    private void validateStatusChange(Post post, PostStatus newStatus) {
        if (!isValidStatusTransition(post.getStatus(), newStatus)) {
            throw new PostStatusChangeException(post.getId(), post.getStatus(), newStatus, POST_CANNOT_CHANGE_STATUS);
        }

        // 발행 조건 검증
        if (newStatus == PostStatus.PUBLISHED && !canPublishPost(post)) {
            throw new PostPublishException(post.getId(), "발행 조건을 만족하지 않습니다", POST_PUBLISH_INVALID_CONDITION);
        }
    }


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


    private Post updatePostFromCommand(Post existingPost, PostUpdateRequest request) {
        return new Post(
                existingPost.getId(),
                existingPost.getUserId(),
                request.categoryId() != null ? request.categoryId() : existingPost.getCategoryId(),
                request.locationId() != null ? request.locationId() : existingPost.getLocationId(),
                request.title() != null ? request.title() : existingPost.getTitle(),
                request.content() != null ? request.content() : existingPost.getContent(),
                existingPost.getSummary(),
                request.status() != null ? request.status() : existingPost.getStatus(),
                request.travelDate() != null ? request.travelDate() : existingPost.getTravelDate(),
                existingPost.getViewCount(),
                existingPost.getLikeCount(),
                existingPost.getCommentCount(),
                request.isFeatured() != null ? request.isFeatured() : existingPost.getIsFeatured(),
                request.status() == PostStatus.PUBLISHED && existingPost.getPublishedAt() == null
                        ? LocalDateTime.now() : existingPost.getPublishedAt()
        );
    }
}