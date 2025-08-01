package backend.airo.api.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 좋아요 응답 DTO
 */
@Schema(description = "좋아요 응답")
public record LikeResponse(
        @Schema(description = "좋아요 상태", example = "true")
        boolean liked,

        @Schema(description = "메시지", example = "좋아요 추가")
        String message
) {
}