package backend.airo.persistence.post.repository;

import backend.airo.api.post.dto.PostSummaryResponse;
import backend.airo.domain.post.enums.PostEmotionTag;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.persistence.post.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * Post JPA Repository
 * Spring Data JPA를 활용한 데이터 접근 인터페이스
 */
@Repository
public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {

    // ===== 기본 조회 메서드 =====

    Page<PostEntity> findByStatus(
            PostStatus status,
            Pageable pageable
    );

    @Modifying
    @Query("update PostEntity p set p.likeCount = p.likeCount + 1 where p.id = :postId")
    int incrementLikeCount(Long postId);

    @Modifying
    @Query("update PostEntity p set p.likeCount = case when p.likeCount>0 then p.likeCount-1 else 0 end where p.id=:postId")
    int decrementLikeCount(Long postId);


    // 좋아요 순으로 게시물 조회
    @Query("select p from PostEntity p order by p.likeCount desc")
    Page<PostEntity> findAllOrderByLikeCountDesc(Pageable pageable);

    // 조회 순으로 게시물 조회
    @Query("select p from PostEntity p order by p.viewCount desc")
    Page<PostEntity> findAllOrderByViewCountDesc(Pageable pageable);

    Slice<PostEntity> findByStatusOrderByIdDesc(PostStatus status, Pageable pageable);

    Slice<PostEntity> findByStatusAndIdLessThanOrderByIdDesc(
            PostStatus status,
            Long id,
            Pageable pageable
    );


    @Query("SELECT p.emotionTags FROM PostEntity p WHERE p.id = :postId")
    Optional<Set<PostEmotionTag>> findEmotionTagsByPostId(@Param("postId") Long postId);

    @Query("SELECT new backend.airo.api.post.dto.PostSummaryResponse(" +
            "p.id, p.title, p.content, p.status, p.viewCount, p.userId) " +
            "FROM PostEntity p WHERE p.id = :postId")
    Optional<PostSummaryResponse> findPostSummaryWithoutEmotionTags(@Param("postId") Long postId);

    @Query("SELECT MAX(p.id) FROM PostEntity p WHERE p.status = 'PUBLISHED'")
    Optional<Long> findMaxPostId();

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM PostEntity p WHERE p.id < :postId AND p.status = 'PUBLISHED'")
    boolean existsByIdLessThan(@Param("postId") Long postId);

    void deleteByUserId(Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
            UPDATE posts
            SET view_count = view_count + 1
            WHERE id = :postId
            """, nativeQuery = true)
    void upsertPostViewCountById(Long postId);
}
