package backend.airo.persistence.post.adapter;

import backend.airo.domain.post.Post;
import backend.airo.domain.post.repository.PostRepository;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.exception.PostNotFoundException;
import backend.airo.persistence.post.entity.PostEntity;
import backend.airo.persistence.post.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static backend.airo.domain.post.exception.PostErrorCode.POST_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostAdapter implements PostRepository {

    private final PostJpaRepository postJpaRepository;


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


            log.debug("Entity 변환 완료: userId={}, title={}",
                    entity.getUserId() != null ? entity.getUserId() : null, entity.getTitle());
        } else {
            log.debug("기존 게시물 업데이트: id={}", post.getId());
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
    public Page<Post> findByStatus(PostStatus status, Pageable pageable) {
        Page<PostEntity> entities = postJpaRepository.findByStatus(status, pageable);
        log.info("Repository 조회 결과 - Entity 개수: {}", entities.getTotalElements());
        return entities.map(PostEntity::toDomain);
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


    @Override
    public int incrementLikeCount(Long postId) {
        return postJpaRepository.incrementLikeCount(postId);
    }

    @Override
    public int decrementLikeCount(Long postId) {
        return postJpaRepository.decrementLikeCount(postId);
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


    @Override
    public Page<Post> findAllOrderByLikeCountDesc(Pageable pageable) {
        Page<PostEntity> entities = postJpaRepository.findAllOrderByLikeCountDesc(pageable);
        return entities.map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> findAllOrderByViewCountDesc(Pageable pageable) {
        Page<PostEntity> entities = postJpaRepository.findAllOrderByViewCountDesc(pageable);
        return entities.map(PostEntity::toDomain);
    }



    // ===== Private Helper Methods =====

    private PostEntity updateExistingEntity(Post post) {
        Optional<PostEntity> existingEntity = postJpaRepository.findById(post.getId());

        return PostEntity.toEntity(post);
    }
}