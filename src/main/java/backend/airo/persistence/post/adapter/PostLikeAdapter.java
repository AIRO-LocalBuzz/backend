package backend.airo.persistence.post.adapter;

import backend.airo.domain.post.PostLike;
import backend.airo.domain.post.repository.PostLikeRepository;
import backend.airo.persistence.post.entity.PostLikeEntity;
import backend.airo.persistence.post.repository.PostLikeJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostLikeAdapter implements PostLikeRepository {

    private final PostLikeJpaRepository postLikeJpaRepository;

    @Override
    public PostLike save(PostLike aggregate) {
        return null;
    }

    @Override
    public Collection<PostLike> saveAll(Collection<PostLike> aggregates) {
        return null;
    }

    @Override
    public PostLike findById(Long aLong) {
        return null;
    }

    //업데이트 -> 1, 중복 -> 0
    @Override
    public int upsertLike(Long postId, Long userId) {
        return postLikeJpaRepository.upsertLike(postId, userId);
    }

    @Override
    public int deleteByPostIdAndUserId(Long postId, Long userId) {
        return postLikeJpaRepository.deleteByPostIdAndUserId(postId, userId);
    }
}
