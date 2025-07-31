package backend.airo.persistence.post.adapter;

import backend.airo.domain.image.Image;
import backend.airo.domain.image.exception.ImageNotFoundException;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.repository.PostRepository;
import backend.airo.domain.post.dto.PostSearchCriteria;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.exception.PostNotFoundException;
import backend.airo.persistence.image.entity.ImageEntity;
import backend.airo.persistence.post.entity.PostEntity;
import backend.airo.persistence.post.repository.PostJpaRepository;
import backend.airo.persistence.user.entity.UserEntity;
import backend.airo.persistence.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static backend.airo.domain.post.exception.PostErrorCode.POST_NOT_FOUND;

/**
 * PostRepository 구현체
 * Infrastructure Layer에서 Domain Layer의 Repository 인터페이스를 구현
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostAdapter implements PostRepository {

    private final PostJpaRepository postJpaRepository;
    private final UserJpaRepository userJpaRepository;

    // ===== CRUD 메서드 =====

    @Override
    @Transactional
    public Post save(Post post) {
        log.debug("게시물 저장 시작: title={}, userId={}, id={}",
                post.getTitle(), post.getUserId(), post.getId());

        PostEntity entity;
        if (post.getId() == null) {
            log.debug("신규 게시물 생성: title={}", post.getTitle());
            entity = PostEntity.toEntity(post);

            // UserEntity 설정
            if (post.getUserId() != null) {
                UserEntity userEntity = userJpaRepository.getReferenceById(post.getUserId());
                entity.setUser(userEntity);
            }

            log.debug("Entity 변환 완료: userId={}, title={}",
                    entity.getUser() != null ? entity.getUser().getId() : null, entity.getTitle());
        } else {
            log.debug("기존 게시물 업데이트: id={}", post.getId());
            entity = updateExistingEntity(post);
        }

        log.debug("JPA Repository 저장 시작: entityId={}", entity.getId());
        PostEntity savedEntity = postJpaRepository.save(entity);

        log.debug("JPA Repository 저장 완료: savedId={}, userId={}",
                savedEntity.getId(),
                savedEntity.getUser() != null ? savedEntity.getUser().getId() : null);

        return PostEntity.toDomain(savedEntity);
    }


    @Override
    public Post findById(Long id) {
        log.debug("게시물 조회: ID={}", id);
        PostEntity postEntity = postJpaRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id, POST_NOT_FOUND));

        return PostEntity.toDomain(postEntity);
    }


    @Override
    public boolean existsById(Long id) {
        return postJpaRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("게시물 삭제: ID={}", id);
        postJpaRepository.deleteById(id);
    }

//    @Override
//    public Collection<Post> saveAll(List<Post> posts) {
//        return List.of();
//    }
    @Override
    public Page<Post> findByStatusAndPublishedAtIsNotNullOrderByPublishedAtDesc(
        PostStatus status,
        Pageable pageable) {
    return postJpaRepository.findByStatusAndPublishedAtIsNotNullOrderByPublishedAtDesc(status, pageable);
    }

    @Override
    @Transactional
    public Collection<Post> saveAll(Collection<Post> posts) {
        log.debug("게시물 일괄 저장: {} 건", posts.size());

        List<PostEntity> entities = posts.stream()
                .map(PostEntity::toEntity)
                .toList();

        List<PostEntity> savedEntities = postJpaRepository.saveAll(entities);

        return savedEntities.stream()
                .map(PostEntity::toDomain)
                .toList();
    }



    // ===== 비즈니스 조회 메서드 =====

    @Override
    public Page<Post> findByUserId(Long userId, Pageable pageable) {
        log.debug("사용자별 게시물 조회: userId={}, page={}", userId, pageable.getPageNumber());

        return postJpaRepository.findByUserId(userId, pageable)
                .map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> findByUserIdAndStatus(Long userId, PostStatus status, Pageable pageable) {
        return postJpaRepository.findByUserIdAndStatus(userId, status, pageable)
                .map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> findByCategoryId(Long categoryId, Pageable pageable) {
        return postJpaRepository.findByCategoryId(categoryId, pageable)
                .map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> findByLocationId(Long locationId, Pageable pageable) {
        return postJpaRepository.findByLocationId(locationId, pageable)
                .map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> findByStatusIn(List<PostStatus> statuses, Pageable pageable) {
        return postJpaRepository.findByStatusIn(statuses, pageable)
                .map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> findFeaturedPosts(Pageable pageable) {
        return postJpaRepository.findByIsFeaturedTrueAndStatus(PostStatus.PUBLISHED, pageable)
                .map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> findPublishedPosts(Pageable pageable) {
        return postJpaRepository.findByStatus(PostStatus.PUBLISHED, pageable)
                .map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> findByTags(List<String> tags, Pageable pageable) {
        return postJpaRepository.findByTagsAndStatus(tags, PostStatus.PUBLISHED, pageable)
                .map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> findByCriteria(PostSearchCriteria criteria, Pageable pageable) {
        log.debug("복합 조건 검색: keyword={}, userId={}", criteria.keyword(), criteria.userId());

        return postJpaRepository.findByCriteria(
                criteria.keyword(),
                criteria.userId(),
                criteria.categoryId(),
                criteria.locationId(),
                criteria.statuses() != null && !criteria.statuses().isEmpty()
                        ? criteria.statuses().get(0) : null,
                criteria.isFeatured(),
                criteria.startDate(),
                criteria.endDate(),
                pageable
        ).map(PostEntity::toDomain);
    }

    // ===== 날짜 범위 조회 메서드 =====

    @Override
    public Page<Post> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return postJpaRepository.findByCreatedAtBetween(startDate, endDate, pageable)
                .map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> findByPublishedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return postJpaRepository.findByPublishedAtBetween(startDate, endDate, pageable)
                .map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> findByTravelDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return postJpaRepository.findByTravelDateBetween(startDate, endDate, pageable)
                .map(PostEntity::toDomain);
    }

    // ===== 통계 및 집계 메서드 =====

    @Override
    public long countByUserId(Long userId) {
        return postJpaRepository.countByUserId(userId);
    }

    @Override
    public long countByUserIdAndStatus(Long userId, PostStatus status) {
        return postJpaRepository.countByUserIdAndStatus(userId, status);
    }

    @Override
    public long countByCategoryId(Long categoryId) {
        return postJpaRepository.countByCategoryId(categoryId);
    }

    @Override
    public long countPublishedPosts() {
        return postJpaRepository.countByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> findPopularPosts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return postJpaRepository.findTopByOrderByViewCountDesc(PostStatus.PUBLISHED, pageable)
                .stream()
                .map(PostEntity::toDomain)
                .toList();
    }

    @Override
    public List<Post> findRecentPopularPosts(int days, int limit) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        Pageable pageable = PageRequest.of(0, limit);

        return postJpaRepository.findRecentPopularPosts(PostStatus.PUBLISHED, since, pageable)
                .stream()
                .map(PostEntity::toDomain)
                .toList();
    }

    @Override
    public List<Post> findMostLikedPosts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return postJpaRepository.findTopByOrderByLikeCountDesc(PostStatus.PUBLISHED, pageable)
                .stream()
                .map(PostEntity::toDomain)
                .toList();
    }

    // ===== 검색 메서드 =====

    @Override
    public Page<Post> findByTitleContaining(String keyword, Pageable pageable) {
        return postJpaRepository.findByTitleContainingAndStatus(keyword, PostStatus.PUBLISHED, pageable)
                .map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> findByContentContaining(String keyword, Pageable pageable) {
        return postJpaRepository.findByContentContainingAndStatus(keyword, PostStatus.PUBLISHED, pageable)
                .map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> findByTitleOrContentContaining(String keyword, Pageable pageable) {
        return postJpaRepository.findByTitleOrContentContaining(keyword, PostStatus.PUBLISHED, pageable)
                .map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> searchFullText(String keyword, Pageable pageable) {
        return postJpaRepository.searchFullText(keyword, PostStatus.PUBLISHED, pageable)
                .map(PostEntity::toDomain);
    }

    // ===== 연관 관계 조회 메서드 =====

    @Override
    public Optional<Post> findByIdWithAssociations(Long id, boolean includeImages, boolean includeTags, boolean includeComments) {
        try {
            if (includeImages && includeTags && includeComments) {
                return postJpaRepository.findByIdWithAllAssociations(id)
                        .map(PostEntity::toDomain);
            } else if (includeImages) {
                return postJpaRepository.findByIdWithImages(id)
                        .map(PostEntity::toDomain);
            } else if (includeTags) {
                return postJpaRepository.findByIdWithTags(id)
                        .map(PostEntity::toDomain);
            } else if (includeComments) {
                return postJpaRepository.findByIdWithComments(id)
                        .map(PostEntity::toDomain);
            }

            return Optional.of(findById(id));
        } catch (PostNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Post> findByIdWithAuthor(Long id) {
        return postJpaRepository.findByIdWithAuthor(id)
                .map(PostEntity::toDomain);
    }

    @Override
    public Optional<Post> findByIdWithLocation(Long id) {
        return postJpaRepository.findByIdWithLocation(id)
                .map(PostEntity::toDomain);
    }

    // ===== 업데이트 메서드 =====


    @Override
    @Transactional
    public boolean updateLikeCount(Long id, int likeCount) {
        int updatedRows = postJpaRepository.updateLikeCount(id, likeCount);
        return updatedRows > 0;
    }

    @Override
    @Transactional
    public boolean updateCommentCount(Long id, int commentCount) {
        int updatedRows = postJpaRepository.updateCommentCount(id, commentCount);
        return updatedRows > 0;
    }

    @Override
    @Transactional
    public boolean updateStatus(Long id, PostStatus status) {
        int updatedRows = postJpaRepository.updateStatus(id, status);
        log.debug("게시물 상태 변경: ID={}, 상태={}, 업데이트 건수={}", id, status, updatedRows);
        return updatedRows > 0;
    }

    @Override
    @Transactional
    public boolean publishPost(Long id, LocalDateTime publishedAt) {
        int updatedRows = postJpaRepository.publishPost(id, PostStatus.PUBLISHED, publishedAt);
        return updatedRows > 0;
    }

    // ===== 배치 처리 메서드 =====

    @Override
    @Transactional
    public int updateStatusBatch(List<Long> ids, PostStatus status) {
        log.debug("게시물 상태 일괄 변경: {} 건, 상태={}", ids.size(), status);
        return postJpaRepository.updateStatusBatch(ids, status);
    }

    @Override
    @Transactional
    public int deleteOldDraftPosts(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);

        List<PostEntity> oldDrafts = postJpaRepository.findOldDraftPosts(PostStatus.DRAFT, cutoffDate);
        if (!oldDrafts.isEmpty()) {
            List<Long> idsToDelete = oldDrafts.stream()
                    .map(PostEntity::getId)
                    .toList();

            postJpaRepository.deleteAllById(idsToDelete);
            log.info("오래된 임시저장 게시물 삭제: {} 건", oldDrafts.size());
        }

        return oldDrafts.size();
    }

    @Override
    @Transactional
    public int archiveInactiveUserPosts(List<Long> userIds) {
        List<PostEntity> postsToArchive = postJpaRepository.findByUserIdsAndStatus(userIds, PostStatus.PUBLISHED);

        if (!postsToArchive.isEmpty()) {
            List<Long> postIds = postsToArchive.stream()
                    .map(PostEntity::getId)
                    .toList();

            int archivedCount = postJpaRepository.updateStatusBatch(postIds, PostStatus.ARCHIVED);
            log.info("비활성 사용자 게시물 보관: {} 건", archivedCount);
            return archivedCount;
        }

        return 0;
    }

    // ===== Private Helper Methods =====

    private PostEntity updateExistingEntity(Post post) {
        PostEntity existingEntity = postJpaRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다: ID=" + post.getId()));

        // 엔티티 필드 업데이트 로직
        // 실제로는 PostEntity에 update 메서드를 만들어서 처리하는 것이 좋음
        return PostEntity.toEntity(post);
    }
}