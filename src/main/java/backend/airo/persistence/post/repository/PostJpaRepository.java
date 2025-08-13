package backend.airo.persistence.post.repository;

import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.persistence.post.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
}