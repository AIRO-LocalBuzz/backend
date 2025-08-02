package backend.airo.domain.post.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.post.PostLike;

public interface PostLikeRepository extends AggregateSupport<PostLike, Long> {

    int upsertLike(Long postId, Long userId);

    int deleteByPostIdAndUserId(Long postId, Long userId);

}
