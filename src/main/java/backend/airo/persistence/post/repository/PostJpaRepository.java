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

    Page<Post> findByStatusAndPublishedAtIsNotNullOrderByPublishedAtDesc(
            PostStatus status,
            Pageable pageable
    );

    /**
     * 사용자별 게시물 조회
     */
    Page<PostEntity> findByUserId(Long userId, Pageable pageable);

    /**
     * 사용자의 특정 상태 게시물 조회
     */
    Page<PostEntity> findByUserIdAndStatus(Long userId, PostStatus status, Pageable pageable);

    /**
     * 카테고리별 게시물 조회
     */
    @Query("SELECT p FROM PostEntity p WHERE p.category.id = :categoryId")
    Page<PostEntity> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    /**
     * 위치별 게시물 조회
     */
    @Query("SELECT p FROM PostEntity p WHERE p.location.id = :locationId")
    Page<PostEntity> findByLocationId(@Param("locationId") Long locationId, Pageable pageable);

    /**
     * 상태별 게시물 조회
     */
    Page<PostEntity> findByStatusIn(List<PostStatus> statuses, Pageable pageable);

    /**
     * 추천 게시물 조회
     */
    Page<PostEntity> findByIsFeaturedTrueAndStatus(PostStatus status, Pageable pageable);

    /**
     * 발행된 게시물 조회
     */
    Page<PostEntity> findByStatus(PostStatus status, Pageable pageable);

    // ===== 연관 관계 포함 조회 =====

    /**
     * 작성자 정보와 함께 조회
     */
    @Query("SELECT p FROM PostEntity p " +
            "LEFT JOIN FETCH p.user " +
            "WHERE p.id = :id")
    Optional<PostEntity> findByIdWithAuthor(@Param("id") Long id);

    /**
     * 위치 정보와 함께 조회
     */
    @Query("SELECT p FROM PostEntity p " +
            "LEFT JOIN FETCH p.location " +
            "WHERE p.id = :id")
    Optional<PostEntity> findByIdWithLocation(@Param("id") Long id);

    /**
     * 이미지와 함께 조회
     */
    @Query("SELECT p FROM PostEntity p " +
            "LEFT JOIN FETCH p.images i " +
            "WHERE p.id = :id " +
            "ORDER BY i.sortOrder")
    Optional<PostEntity> findByIdWithImages(@Param("id") Long id);

    /**
     * 태그와 함께 조회
     */
    @Query("SELECT p FROM PostEntity p " +
            "LEFT JOIN FETCH p.postTags pt " +
            "LEFT JOIN FETCH pt.tag " +
            "WHERE p.id = :id")
    Optional<PostEntity> findByIdWithTags(@Param("id") Long id);

    /**
     * 댓글과 함께 조회
     */
    @Query("SELECT p FROM PostEntity p " +
            "LEFT JOIN FETCH p.comments c " +
            "WHERE p.id = :id " +
            "ORDER BY c.createdAt")
    Optional<PostEntity> findByIdWithComments(@Param("id") Long id);

    /**
     * 모든 연관 데이터와 함께 조회
     */
    @Query("SELECT DISTINCT p FROM PostEntity p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.category " +
            "LEFT JOIN FETCH p.location " +
            "LEFT JOIN FETCH p.images " +
            "LEFT JOIN FETCH p.postTags pt " +
            "LEFT JOIN FETCH pt.tag " +
            "WHERE p.id = :id")
    Optional<PostEntity> findByIdWithAllAssociations(@Param("id") Long id);

    // ===== 날짜 범위 조회 =====

    /**
     * 생성일 기준 범위 조회
     */
    Page<PostEntity> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * 발행일 기준 범위 조회
     */
    Page<PostEntity> findByPublishedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * 여행일 기준 범위 조회
     */
    Page<PostEntity> findByTravelDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // ===== 검색 메서드 =====

    /**
     * 제목 검색
     */
    Page<PostEntity> findByTitleContainingAndStatus(String keyword, PostStatus status, Pageable pageable);

    /**
     * 내용 검색
     */
    Page<PostEntity> findByContentContainingAndStatus(String keyword, PostStatus status, Pageable pageable);

    /**
     * 제목 또는 내용 검색
     */
    @Query("SELECT p FROM PostEntity p " +
            "WHERE (p.title LIKE %:keyword% OR p.content LIKE %:keyword%) " +
            "AND p.status = :status")
    Page<PostEntity> findByTitleOrContentContaining(@Param("keyword") String keyword,
                                                    @Param("status") PostStatus status,
                                                    Pageable pageable);

    /**
     * 전문 검색 (제목, 내용, 요약)
     */
    @Query("SELECT p FROM PostEntity p " +
            "WHERE (p.title LIKE %:keyword% OR p.content LIKE %:keyword% OR p.summary LIKE %:keyword%) " +
            "AND p.status = :status")
    Page<PostEntity> searchFullText(@Param("keyword") String keyword,
                                    @Param("status") PostStatus status,
                                    Pageable pageable);

    /**
     * 태그별 게시물 조회
     */
    @Query("SELECT DISTINCT p FROM PostEntity p " +
            "JOIN p.postTags pt " +
            "JOIN pt.tag t " +
            "WHERE t.name IN :tags " +
            "AND p.status = :status")
    Page<PostEntity> findByTagsAndStatus(@Param("tags") List<String> tags,
                                         @Param("status") PostStatus status,
                                         Pageable pageable);

    // ===== 복합 조건 검색 =====

    /**
     * 복합 조건 검색 - 동적 쿼리용
     */
    @Query("SELECT DISTINCT p FROM PostEntity p " +
            "LEFT JOIN p.postTags pt " +
            "LEFT JOIN pt.tag t " +
            "WHERE (:keyword IS NULL OR p.title LIKE %:keyword% OR p.content LIKE %:keyword%) " +
            "AND (:userId IS NULL OR p.user.id = :userId) " +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
            "AND (:locationId IS NULL OR p.location.id = :locationId) " +
            "AND (:status IS NULL OR p.status = :status) " +
            "AND (:isFeatured IS NULL OR p.isFeatured = :isFeatured) " +
            "AND (:startDate IS NULL OR p.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR p.createdAt <= :endDate)")
    Page<PostEntity> findByCriteria(@Param("keyword") String keyword,
                                    @Param("userId") Long userId,
                                    @Param("categoryId") Long categoryId,
                                    @Param("locationId") Long locationId,
                                    @Param("status") PostStatus status,
                                    @Param("isFeatured") Boolean isFeatured,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate,
                                    Pageable pageable);

    // ===== 통계 및 집계 =====

    /**
     * 사용자별 게시물 수
     */
    long countByUserId(Long userId);

    /**
     * 사용자의 특정 상태 게시물 수
     */
    long countByUserIdAndStatus(Long userId, PostStatus status);

    /**
     * 카테고리별 게시물 수
     */
    @Query("SELECT COUNT(p) FROM PostEntity p WHERE p.category.id = :categoryId")
    long countByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 발행된 게시물 수
     */
    long countByStatus(PostStatus status);

    /**
     * 인기 게시물 조회 (조회수 기준)
     */
    @Query("SELECT p FROM PostEntity p " +
            "WHERE p.status = :status " +
            "ORDER BY p.viewCount DESC")
    List<PostEntity> findTopByOrderByViewCountDesc(@Param("status") PostStatus status, Pageable pageable);

    /**
     * 최근 인기 게시물 조회
     */
    @Query("SELECT p FROM PostEntity p " +
            "WHERE p.status = :status " +
            "AND p.createdAt >= :since " +
            "ORDER BY p.viewCount DESC")
    List<PostEntity> findRecentPopularPosts(@Param("status") PostStatus status,
                                            @Param("since") LocalDateTime since,
                                            Pageable pageable);

    /**
     * 좋아요 많은 게시물 조회
     */
    @Query("SELECT p FROM PostEntity p " +
            "WHERE p.status = :status " +
            "ORDER BY p.likeCount DESC")
    List<PostEntity> findTopByOrderByLikeCountDesc(@Param("status") PostStatus status, Pageable pageable);

    // ===== 업데이트 메서드 =====


    /**
     * 좋아요 수 업데이트
     */
    @Modifying
    @Query("UPDATE PostEntity p SET p.likeCount = :likeCount WHERE p.id = :id")
    int updateLikeCount(@Param("id") Long id, @Param("likeCount") int likeCount);

    /**
     * 댓글 수 업데이트
     */
    @Modifying
    @Query("UPDATE PostEntity p SET p.commentCount = :commentCount WHERE p.id = :id")
    int updateCommentCount(@Param("id") Long id, @Param("commentCount") int commentCount);

    /**
     * 게시물 상태 변경
     */
    @Modifying
    @Query("UPDATE PostEntity p SET p.status = :status WHERE p.id = :id")
    int updateStatus(@Param("id") Long id, @Param("status") PostStatus status);

    /**
     * 게시물 발행
     */
    @Modifying
    @Query("UPDATE PostEntity p SET p.status = :status, p.publishedAt = :publishedAt WHERE p.id = :id")
    int publishPost(@Param("id") Long id, @Param("status") PostStatus status, @Param("publishedAt") LocalDateTime publishedAt);

    // ===== 배치 처리 =====

    /**
     * 여러 게시물 상태 일괄 변경
     */
    @Modifying
    @Query("UPDATE PostEntity p SET p.status = :status WHERE p.id IN :ids")
    int updateStatusBatch(@Param("ids") List<Long> ids, @Param("status") PostStatus status);

    /**
     * 오래된 임시저장 게시물 조회
     */
    @Query("SELECT p FROM PostEntity p " +
            "WHERE p.status = :status " +
            "AND p.createdAt < :cutoffDate")
    List<PostEntity> findOldDraftPosts(@Param("status") PostStatus status, @Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * 비활성 사용자의 게시물 조회
     */
    @Query("SELECT p FROM PostEntity p WHERE p.user.id IN :userIds AND p.status = :status")
    List<PostEntity> findByUserIdsAndStatus(@Param("userIds") List<Long> userIds, @Param("status") PostStatus status);
}