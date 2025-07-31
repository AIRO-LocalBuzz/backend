package backend.airo.domain.post.query;

import backend.airo.domain.post.enums.PostStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시물 목록 조회 쿼리
 */
public record GetPostListQuery(
        // 페이징
        @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다")
        Integer page,

        @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
        @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다")
        Integer size,

        // 정렬
        @Pattern(regexp = "^(createdAt|updatedAt|viewCount|likeCount|title)$",
                message = "정렬 필드는 createdAt, updatedAt, viewCount, likeCount, title 중 하나여야 합니다")
        String sortBy,

        @Pattern(regexp = "^(ASC|DESC)$", message = "정렬 방향은 ASC 또는 DESC여야 합니다")
        String sortDirection,

        // 필터링
        List<PostStatus> statuses,

        @Positive(message = "사용자 ID는 양수여야 합니다")
        Long userId,

        @Positive(message = "카테고리 ID는 양수여야 합니다")
        Long categoryId,

        @Positive(message = "위치 ID는 양수여야 합니다")
        Long locationId,

        List<@NotBlank String> tags,

        Boolean isFeatured,

        // 날짜 범위
        LocalDateTime startDate,
        LocalDateTime endDate,

        // 검색
        @Size(max = 100, message = "검색어는 100자를 초과할 수 없습니다")
        String keyword,

        @Pattern(regexp = "^(title|content|all)$",
                message = "검색 범위는 title, content, all 중 하나여야 합니다")
        String searchScope,

        // 요청자 정보
        Long requesterId,

        // 포함 옵션
        Boolean includeImages,
        Boolean includeTags,
        Boolean includeAuthor
) {

    public GetPostListQuery {
        // 기본값 설정
        if (page == null) page = 0;
        if (size == null) size = 20;
        if (sortBy == null) sortBy = "createdAt";
        if (sortDirection == null) sortDirection = "DESC";
        if (searchScope == null) searchScope = "all";
        if (includeImages == null) includeImages = true;
        if (includeTags == null) includeTags = true;
        if (includeAuthor == null) includeAuthor = true;

        // 날짜 범위 검증
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 빨라야 합니다");
        }
    }

    /**
     * 기본 목록 조회 쿼리
     */
    public static GetPostListQuery defaultQuery() {
        return new GetPostListQuery(
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null
        );
    }

    /**
     * 발행된 게시물만 조회
     */
    public static GetPostListQuery publishedOnly() {
        return new GetPostListQuery(
                null, null, null, null, List.of(PostStatus.PUBLISHED), null, null, null, null, null,
                null, null, null, null, null, null, null, null
        );
    }

    /**
     * 사용자별 게시물 조회
     */
    public static GetPostListQuery byUser(Long userId) {
        return new GetPostListQuery(
                null, null, null, null, null, userId, null, null, null, null,
                null, null, null, null, null, null, null, null
        );
    }

    /**
     * 카테고리별 게시물 조회
     */
    public static GetPostListQuery byCategory(Long categoryId) {
        return new GetPostListQuery(
                null, null, null, null, List.of(PostStatus.PUBLISHED), null, categoryId, null, null, null,
                null, null, null, null, null, null, null, null
        );
    }

    /**
     * 태그별 게시물 조회
     */
    public static GetPostListQuery byTags(List<String> tags) {
        return new GetPostListQuery(
                null, null, null, null, List.of(PostStatus.PUBLISHED), null, null, null, tags, null,
                null, null, null, null, null, null, null, null
        );
    }

    /**
     * 검색 쿼리
     */
    public static GetPostListQuery search(String keyword) {
        return new GetPostListQuery(
                null, null, null, null, List.of(PostStatus.PUBLISHED), null, null, null, null, null,
                null, null, keyword, null, null, null, null, null
        );
    }

    /**
     * 추천 게시물 조회
     */
    public static GetPostListQuery featured() {
        return new GetPostListQuery(
                null, null, "likeCount", "DESC", List.of(PostStatus.PUBLISHED), null, null, null, null, true,
                null, null, null, null, null, null, null, null
        );
    }

    /**
     * 인기 게시물 조회 (조회수 기준)
     */
    public static GetPostListQuery popular() {
        return new GetPostListQuery(
                null, null, "viewCount", "DESC", List.of(PostStatus.PUBLISHED), null, null, null, null, null,
                null, null, null, null, null, null, null, null
        );
    }

    /**
     * 검색어가 있는지 확인
     */
    public boolean hasKeyword() {
        return keyword != null && !keyword.trim().isEmpty();
    }

    /**
     * 필터가 적용되었는지 확인
     */
    public boolean hasFilters() {
        return (statuses != null && !statuses.isEmpty()) ||
                userId != null || categoryId != null || locationId != null ||
                (tags != null && !tags.isEmpty()) || isFeatured != null ||
                startDate != null || endDate != null;
    }

    /**
     * 날짜 범위 필터가 있는지 확인
     */
    public boolean hasDateRange() {
        return startDate != null || endDate != null;
    }

    /**
     * 태그 필터가 있는지 확인
     */
    public boolean hasTagFilter() {
        return tags != null && !tags.isEmpty();
    }

    /**
     * 정렬이 내림차순인지 확인
     */
    public boolean isDescending() {
        return "DESC".equals(sortDirection);
    }
}