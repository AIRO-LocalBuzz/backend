package backend.airo.domain.post.query;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * 게시물 단건 조회 쿼리
 */
public record GetPostQuery(
        @NotNull(message = "게시물 ID는 필수입니다")
        @Positive(message = "게시물 ID는 양수여야 합니다")
        Long postId,

        Long requesterId,

        Boolean includeImages,

        Boolean includeTags,

        Boolean includeComments,

        Boolean includeAuthor,

        Boolean includeLocation,

        Boolean includeCategory,

        Boolean incrementViewCount
) {

    public GetPostQuery {
        if (includeImages == null) includeImages = true;
        if (includeTags == null) includeTags = true;
        if (includeComments == null) includeComments = false;
        if (includeAuthor == null) includeAuthor = true;
        if (includeLocation == null) includeLocation = true;
        if (includeCategory == null) includeCategory = true;
        if (incrementViewCount == null) incrementViewCount = true;
    }

    /**
     * 기본 조회 쿼리 생성
     */
    public static GetPostQuery of(Long postId) {
        return new GetPostQuery(postId, null, null, null, null, null, null, null, null);
    }

    /**
     * 사용자별 조회 쿼리 생성 (조회수 증가 포함)
     */
    public static GetPostQuery byUser(Long postId, Long requesterId) {
        return new GetPostQuery(postId, requesterId, null, null, null, null, null, null, true);
    }

    /**
     * 상세 조회 쿼리 생성 (모든 연관 데이터 포함)
     */
    public static GetPostQuery withDetails(Long postId) {
        return new GetPostQuery(postId, null, true, true, true, true, true, true, null);
    }

    /**
     * 간단 조회 쿼리 생성 (기본 데이터만)
     */
    public static GetPostQuery simple(Long postId) {
        return new GetPostQuery(postId, null, false, false, false, false, false, false, false);
    }

    /**
     * 미리보기용 조회 쿼리 생성 (조회수 증가 없음)
     */
    public static GetPostQuery preview(Long postId) {
        return new GetPostQuery(postId, null, true, true, false, true, true, true, false);
    }

    /**
     * 조회수 증가 필요 여부
     */
    public boolean shouldIncrementViewCount() {
        return incrementViewCount != null && incrementViewCount;
    }

    /**
     * 연관 데이터 포함 여부 확인
     */
    public boolean hasIncludes() {
        return includeImages || includeTags || includeComments ||
                includeAuthor || includeLocation || includeCategory;
    }
}