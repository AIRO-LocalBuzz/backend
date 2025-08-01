package backend.airo.domain.post.dto;

import backend.airo.domain.post.enums.PostStatus;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시물 검색 조건을 위한 Criteria 클래스
 */
public record PostSearchCriteria(
        String keyword,
        String searchScope, // title, content, all
        List<PostStatus> statuses,
        Long userId,
        Long categoryId,
        Long locationId,
        List<String> tags,
        Boolean isFeatured,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime travelStartDate,
        LocalDateTime travelEndDate,
        Integer minViewCount,
        Integer minLikeCount,
        Integer minCommentCount
) {

    /**
     * 기본 검색 조건 생성
     */
    public static PostSearchCriteria defaultCriteria() {
        return new PostSearchCriteria(
                null, "all", List.of(PostStatus.PUBLISHED), null, null, null,
                null, null, null, null, null, null, null, null, null
        );
    }

    /**
     * 키워드 검색 조건 생성
     */
    public static PostSearchCriteria forKeyword(String keyword) {
        return new PostSearchCriteria(
                keyword, "all", List.of(PostStatus.PUBLISHED), null, null, null,
                null, null, null, null, null, null, null, null, null
        );
    }

    /**
     * 사용자별 검색 조건 생성
     */
    public static PostSearchCriteria forUser(Long userId) {
        return new PostSearchCriteria(
                null, "all", null, userId, null, null,
                null, null, null, null, null, null, null, null, null
        );
    }

    /**
     * 고급 검색 조건 생성
     */
    public static PostSearchCriteria advanced(String keyword, List<PostStatus> statuses,
                                              Long categoryId, List<String> tags) {
        return new PostSearchCriteria(
                keyword, "all", statuses, null, categoryId, null,
                tags, null, null, null, null, null, null, null, null
        );
    }

    /**
     * 검색어가 있는지 확인
     */
    public boolean hasKeyword() {
        return keyword != null && !keyword.trim().isEmpty();
    }

    /**
     * 필터 조건이 있는지 확인
     */
    public boolean hasFilters() {
        return (statuses != null && !statuses.isEmpty()) ||
                userId != null || categoryId != null || locationId != null ||
                (tags != null && !tags.isEmpty()) || isFeatured != null ||
                hasDateFilters() || hasMetricFilters();
    }

    /**
     * 날짜 필터가 있는지 확인
     */
    public boolean hasDateFilters() {
        return startDate != null || endDate != null ||
                travelStartDate != null || travelEndDate != null;
    }

    /**
     * 메트릭 필터가 있는지 확인 (조회수, 좋아요, 댓글)
     */
    public boolean hasMetricFilters() {
        return minViewCount != null || minLikeCount != null || minCommentCount != null;
    }

    /**
     * 태그 필터가 있는지 확인
     */
    public boolean hasTagFilter() {
        return tags != null && !tags.isEmpty();
    }
}