package backend.airo.api.post.dto;

import backend.airo.domain.post.enums.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;

/**
 * 게시물 목록 조회 요청 DTO
 */
@Schema(description = "게시물 목록 조회 요청")
public record PostListRequest(
        @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
        @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다")
        Integer page,

        @Schema(description = "페이지 크기", example = "20")
        @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
        @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다")
        Integer size,

        @Schema(description = "정렬 필드", example = "createdAt")
        @Pattern(regexp = "^(createdAt|updatedAt|viewCount|likeCount|title)$",
                message = "정렬 필드는 createdAt, updatedAt, viewCount, likeCount, title 중 하나여야 합니다")
        String sortBy,

        @Schema(description = "정렬 방향", example = "DESC")
        @Pattern(regexp = "^(ASC|DESC)$", message = "정렬 방향은 ASC 또는 DESC여야 합니다")
        String sortDirection,

        @Schema(description = "게시물 상태 목록", example = "[\"PUBLISHED\"]")
        List<PostStatus> statuses,

        @Schema(description = "작성자 ID", example = "1")
        @Positive(message = "작성자 ID는 양수여야 합니다")
        Long userId,

        @Schema(description = "카테고리 ID", example = "1")
        @Positive(message = "카테고리 ID는 양수여야 합니다")
        Long categoryId,

        @Schema(description = "위치 ID", example = "1")
        @Positive(message = "위치 ID는 양수여야 합니다")
        Long locationId,

        @Schema(description = "태그 목록", example = "[\"부산\", \"여행\"]")
        @Size(max = 10, message = "태그는 최대 10개까지 검색 가능합니다")
        List<@NotBlank @Size(max = 50) String> tags,

        @Schema(description = "추천 게시물 여부", example = "true")
        Boolean isFeatured,

        @Schema(description = "검색 키워드", example = "부산 여행")
        @Size(max = 100, message = "검색어는 100자를 초과할 수 없습니다")
        String keyword,

        @Schema(description = "검색 범위", example = "all")
        @Pattern(regexp = "^(title|content|all)$",
                message = "검색 범위는 title, content, all 중 하나여야 합니다")
        String searchScope
) {
    public PostListRequest {
        // 기본값 설정
        if (page == null) page = 0;
        if (size == null) size = 20;
        if (sortBy == null) sortBy = "createdAt";
        if (sortDirection == null) sortDirection = "DESC";
        if (searchScope == null) searchScope = "all";
    }
}