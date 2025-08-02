package backend.airo.api.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * 인기 게시물 조회 요청 DTO
 */
@Schema(description = "인기 게시물 조회 요청")
public record PopularPostsRequest(
        @Schema(description = "조회할 개수", example = "10")
        @Min(value = 1, message = "조회 개수는 1 이상이어야 합니다")
        @Max(value = 50, message = "조회 개수는 50 이하여야 합니다")
        Integer limit
) {
    public PopularPostsRequest {
        if (limit == null) limit = 10;
    }
}