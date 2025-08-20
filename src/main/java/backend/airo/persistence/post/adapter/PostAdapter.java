package backend.airo.persistence.post.adapter;

import backend.airo.api.post.dto.PostSummaryResponse;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostEmotionTag;
import backend.airo.domain.post.exception.PostException;
import backend.airo.domain.post.repository.PostRepository;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.persistence.post.entity.PostEntity;
import backend.airo.persistence.post.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class PostAdapter implements PostRepository {

    private final PostJpaRepository postJpaRepository;


    // ===== CRUD 메서드 =====

    @Override
    public Post save(Post post) {
        log.debug("게시물 저장 시작: title={}, userId={}, id={}",
                post.title(), post.userId(), post.id());

        PostEntity entity;
        if (post.id() == null) {
            log.debug("신규 게시물 생성: title={}", post.title());
            entity = PostEntity.toEntity(post);


            log.debug("Entity 변환 완료: userId={}, title={}",
                    entity.getUserId() != null ? entity.getUserId() : null, entity.getTitle());
        } else {
            log.debug("기존 게시물 업데이트: id={}", post.id());
            entity = updateExistingEntity(post);
        }

        log.debug("JPA Repository 저장 시작: entityId={}", entity.getId());
        PostEntity savedEntity = postJpaRepository.save(entity);

        log.debug("JPA Repository 저장 완료: savedId={}, userId={}",
                savedEntity.getId(),
                savedEntity.getUserId() != null ? savedEntity.getUserId() : null);

        return PostEntity.toDomain(savedEntity);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Post> findByStatus(PostStatus status, Pageable pageable) {
        Page<PostEntity> entities = postJpaRepository.findByStatus(status, pageable);
        log.info("Repository 조회 결과 - Entity 개수: {}", entities.getTotalElements());
        return entities.map(PostEntity::toDomain);
    }



    @Override
    @Transactional(readOnly = true)
    public Post findById(Long id) {
        log.debug("게시물 DB조회: ID={}", id);
        PostEntity postEntity = postJpaRepository.findById(id)
                .orElseThrow(() -> PostException.notFound(id));

        return PostEntity.toDomain(postEntity);
    }


    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return postJpaRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("게시물 삭제: ID={}", id);
        postJpaRepository.deleteById(id);
    }


    @Override
    public int incrementLikeCount(Long postId) {
        return postJpaRepository.incrementLikeCount(postId);
    }

    @Override
    public int decrementLikeCount(Long postId) {
        return postJpaRepository.decrementLikeCount(postId);
    }


    @Override
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


    @Override
    @Transactional(readOnly = true)
    public Page<Post> findAllOrderByLikeCountDesc(Pageable pageable) {
        Page<PostEntity> entities = postJpaRepository.findAllOrderByLikeCountDesc(pageable);
        return entities.map(PostEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findAllOrderByViewCountDesc(Pageable pageable) {
        Page<PostEntity> entities = postJpaRepository.findAllOrderByViewCountDesc(pageable);
        return entities.map(PostEntity::toDomain);
    }


    @Override
    @Transactional(readOnly = true)
    public Slice<Post> findSliceAfterCursor(Long lastPostId, int size) {
        log.debug("커서 기반 게시물 조회: lastPostId={}, size={}", lastPostId, size);

        Pageable pageable = PageRequest.of(0, size);
        Slice<PostEntity> entities;

        if (lastPostId == null) {
            // 첫 번째 요청: 최신 게시물부터 조회
            entities = postJpaRepository.findByStatusOrderByIdDesc(PostStatus.PUBLISHED, pageable);
        } else {
            // 다음 요청: lastPostId보다 작은 ID의 게시물 조회 (최신순)
            entities = postJpaRepository.findByStatusAndIdLessThanOrderByIdDesc(
                    PostStatus.PUBLISHED, lastPostId, pageable);
        }

        log.debug("커서 기반 조회 결과: {} 건, hasNext: {}",
                entities.getNumberOfElements(), entities.hasNext());

        return entities.map(PostEntity::toDomain);
    }


    @Override
    @Transactional(readOnly = true)
    public PostSummaryResponse findPostSummaryById(Long postId) {
        log.debug("게시물 요약 정보 조회: ID={}", postId);

        // 1. 기본 정보 조회
        Optional<PostSummaryResponse> baseResponseOpt = postJpaRepository.findPostSummaryWithoutEmotionTags(postId);
        if (baseResponseOpt.isEmpty()) {
            log.debug("게시물이 존재하지 않음: ID={}", postId);
            return null;
        }

        PostSummaryResponse baseResponse = baseResponseOpt.get();

        // 2. emotionTags 조회
        Set<PostEmotionTag> emotionTags = postJpaRepository.findEmotionTagsByPostId(postId)
                .orElse(Collections.emptySet()); // 감정 태그가 없을 수도 있으므로 빈 Set 사용

        // 3. 합치기
        return new PostSummaryResponse(
                baseResponse.id(),
                baseResponse.title(),
                baseResponse.content(),
                baseResponse.status(),
                baseResponse.viewCount(),
                new ArrayList<>(emotionTags), // Set을 List로 변환
                baseResponse.userId()
        );
    }


    @Override
    @Transactional(readOnly = true)
    public Long findMaxPostId() {
        return postJpaRepository.findMaxPostId()
                .orElseThrow(() -> PostException.notFound(9999L));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIdLessThan(Long id) {
        return postJpaRepository.existsByIdLessThan(id);
    }

    @Override
    public void upsertPostViewCount(Long postId) {
        postJpaRepository.upsertPostViewCountById(postId);
    }

    // ===== Private Helper Methods =====

    private PostEntity updateExistingEntity(Post post) {
        PostEntity existingEntity = postJpaRepository.findById(post.id())
                .orElseThrow(() -> PostException.notFound(post.id()));

        return PostEntity.toEntity(post);
    }
}