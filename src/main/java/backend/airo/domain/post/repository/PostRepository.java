package backend.airo.domain.post.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.dto.PostSearchCriteria;
import backend.airo.domain.post.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Post 도메인 Repository 인터페이스
 * Domain Layer에서 정의하는 비즈니스 저장소 계약
 */
public interface PostRepository extends AggregateSupport<Post, Long> {

    // ===== CRUD 메서드 =====

    /**
     * 게시물 저장
     * @param post 저장할 게시물 도메인 객체
     * @return 저장된 게시물 도메인 객체
     */
    Post save(Post post);

    /**
     * ID로 게시물 조회
     *
     * @param id 게시물 ID
     * @return 게시물 도메인 객체 (Optional)
     */
    Post findById(Long id);

    /**
     * 게시물 존재 여부 확인
     * @param id 게시물 ID
     * @return 존재 여부
     */
    boolean existsById(Long id);

    /**
     * 게시물 삭제
     * @param id 삭제할 게시물 ID
     */
    void deleteById(Long id);

    /**
     * 여러 게시물 일괄 저장
     * @param posts 저장할 게시물 목록
     * @return 저장된 게시물 목록
     */
    Collection<Post> saveAll(Collection<Post> posts);

    // ===== 비즈니스 조회 메서드 =====

    /**
     * 사용자별 게시물 조회 (페이징)
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 게시물 페이지
     */
    Page<Post> findByUserId(Long userId, Pageable pageable);

    /**
     * 사용자의 특정 상태 게시물 조회
     * @param userId 사용자 ID
     * @param status 게시물 상태
     * @param pageable 페이징 정보
     * @return 게시물 페이지
     */
    Page<Post> findByUserIdAndStatus(Long userId, PostStatus status, Pageable pageable);

    /**
     * 카테고리별 게시물 조회
     * @param categoryId 카테고리 ID
     * @param pageable 페이징 정보
     * @return 게시물 페이지
     */
    Page<Post> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * 위치별 게시물 조회
     * @param locationId 위치 ID
     * @param pageable 페이징 정보
     * @return 게시물 페이지
     */
    Page<Post> findByLocationId(Long locationId, Pageable pageable);

    /**
     * 상태별 게시물 조회
     * @param statuses 조회할 상태 목록
     * @param pageable 페이징 정보
     * @return 게시물 페이지
     */
    Page<Post> findByStatusIn(List<PostStatus> statuses, Pageable pageable);

    /**
     * 추천 게시물 조회
     * @param pageable 페이징 정보
     * @return 추천 게시물 페이지
     */
    Page<Post> findFeaturedPosts(Pageable pageable);

    /**
     * 발행된 게시물 조회 (기본)
     * @param pageable 페이징 정보
     * @return 발행된 게시물 페이지
     */
    Page<Post> findPublishedPosts(Pageable pageable);

    /**
     * 태그별 게시물 조회
     * @param tags 태그 목록
     * @param pageable 페이징 정보
     * @return 게시물 페이지
     */
    Page<Post> findByTags(List<String> tags, Pageable pageable);

    /**
     * 복합 조건으로 게시물 검색
     * @param criteria 검색 조건
     * @param pageable 페이징 정보
     * @return 게시물 페이지
     */
    Page<Post> findByCriteria(PostSearchCriteria criteria, Pageable pageable);

    // ===== 날짜 범위 조회 메서드 =====

    /**
     * 생성일 기준 범위 조회
     * @param startDate 시작일
     * @param endDate 종료일
     * @param pageable 페이징 정보
     * @return 게시물 페이지
     */
    Page<Post> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * 발행일 기준 범위 조회
     * @param startDate 시작일
     * @param endDate 종료일
     * @param pageable 페이징 정보
     * @return 게시물 페이지
     */
    Page<Post> findByPublishedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * 여행일 기준 범위 조회
     * @param startDate 시작일
     * @param endDate 종료일
     * @param pageable 페이징 정보
     * @return 게시물 페이지
     */
    Page<Post> findByTravelDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // ===== 통계 및 집계 메서드 =====

    /**
     * 사용자별 게시물 수 조회
     * @param userId 사용자 ID
     * @return 게시물 수
     */
    long countByUserId(Long userId);

    /**
     * 사용자의 특정 상태 게시물 수 조회
     * @param userId 사용자 ID
     * @param status 게시물 상태
     * @return 게시물 수
     */
    long countByUserIdAndStatus(Long userId, PostStatus status);

    /**
     * 카테고리별 게시물 수 조회
     * @param categoryId 카테고리 ID
     * @return 게시물 수
     */
    long countByCategoryId(Long categoryId);

    /**
     * 전체 발행된 게시물 수 조회
     * @return 발행된 게시물 수
     */
    long countPublishedPosts();

    /**
     * 인기 게시물 조회 (조회수 기준)
     * @param limit 조회할 개수
     * @return 인기 게시물 목록
     */
    List<Post> findPopularPosts(int limit);

    /**
     * 최근 인기 게시물 조회 (기간 + 조회수 기준)
     * @param days 최근 일수
     * @param limit 조회할 개수
     * @return 최근 인기 게시물 목록
     */
    List<Post> findRecentPopularPosts(int days, int limit);

    /**
     * 좋아요 많은 게시물 조회
     * @param limit 조회할 개수
     * @return 좋아요 많은 게시물 목록
     */
    List<Post> findMostLikedPosts(int limit);

    // ===== 검색 메서드 =====

    /**
     * 제목으로 게시물 검색
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보
     * @return 검색된 게시물 페이지
     */
    Page<Post> findByTitleContaining(String keyword, Pageable pageable);

    /**
     * 내용으로 게시물 검색
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보
     * @return 검색된 게시물 페이지
     */
    Page<Post> findByContentContaining(String keyword, Pageable pageable);

    /**
     * 제목 또는 내용으로 게시물 검색
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보
     * @return 검색된 게시물 페이지
     */
    Page<Post> findByTitleOrContentContaining(String keyword, Pageable pageable);

    /**
     * 전문 검색 (제목, 내용, 요약 포함)
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보
     * @return 검색된 게시물 페이지
     */
    Page<Post> searchFullText(String keyword, Pageable pageable);

    // ===== 연관 관계 조회 메서드 =====

    /**
     * 연관 데이터와 함께 게시물 조회
     * @param id 게시물 ID
     * @param includeImages 이미지 포함 여부
     * @param includeTags 태그 포함 여부
     * @param includeComments 댓글 포함 여부
     * @return 게시물 도메인 객체 (Optional)
     */
    Optional<Post> findByIdWithAssociations(Long id, boolean includeImages, boolean includeTags, boolean includeComments);

    /**
     * 작성자 정보와 함께 게시물 조회
     * @param id 게시물 ID
     * @return 게시물 도메인 객체 (Optional)
     */
    Optional<Post> findByIdWithAuthor(Long id);

    /**
     * 위치 정보와 함께 게시물 조회
     * @param id 게시물 ID
     * @return 게시물 도메인 객체 (Optional)
     */
    Optional<Post> findByIdWithLocation(Long id);

    // ===== 업데이트 메서드 =====

    /**
     * 조회수 증가
     * @param id 게시물 ID
     * @param incrementBy 증가할 수
     * @return 업데이트 성공 여부
     */
    boolean incrementViewCount(Long id, int incrementBy);

    /**
     * 좋아요 수 업데이트
     * @param id 게시물 ID
     * @param likeCount 새로운 좋아요 수
     * @return 업데이트 성공 여부
     */
    boolean updateLikeCount(Long id, int likeCount);

    /**
     * 댓글 수 업데이트
     * @param id 게시물 ID
     * @param commentCount 새로운 댓글 수
     * @return 업데이트 성공 여부
     */
    boolean updateCommentCount(Long id, int commentCount);

    /**
     * 게시물 상태 변경
     * @param id 게시물 ID
     * @param status 새로운 상태
     * @return 업데이트 성공 여부
     */
    boolean updateStatus(Long id, PostStatus status);

    /**
     * 게시물 발행
     * @param id 게시물 ID
     * @param publishedAt 발행 시간
     * @return 업데이트 성공 여부
     */
    boolean publishPost(Long id, LocalDateTime publishedAt);

    // ===== 배치 처리 메서드 =====

    /**
     * 여러 게시물 상태 일괄 변경
     * @param ids 게시물 ID 목록
     * @param status 새로운 상태
     * @return 업데이트된 게시물 수
     */
    int updateStatusBatch(List<Long> ids, PostStatus status);

    /**
     * 오래된 임시저장 게시물 삭제
     * @param days 일수 (며칠 전)
     * @return 삭제된 게시물 수
     */
    int deleteOldDraftPosts(int days);

    /**
     * 비활성 사용자의 게시물 보관
     * @param userIds 비활성 사용자 ID 목록
     * @return 보관된 게시물 수
     */
    int archiveInactiveUserPosts(List<Long> userIds);
}