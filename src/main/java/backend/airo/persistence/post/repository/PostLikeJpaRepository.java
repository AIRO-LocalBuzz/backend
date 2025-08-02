package backend.airo.persistence.post.repository;

import backend.airo.persistence.post.entity.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeJpaRepository extends JpaRepository<PostLikeEntity, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
            INSERT IGNORE INTO post_like(post_id, user_id)
            VALUES (:postId, :userId)
            """, nativeQuery = true)
    int upsertLike(@Param("postId") Long postId, @Param("userId") Long userId);

    int deleteByPostIdAndUserId(Long postId, Long userId);

}
