package backend.airo.api.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "이미지 통계 응답")
public record ImageStatsResponse(
        @Schema(description = "게시물 ID", example = "1")
        Long postId,

        @Schema(description = "이미지 개수", example = "5")
        Integer imageCount,

        @Schema(description = "총 이미지 크기 (bytes)", example = "5242880")
        Long totalSize,

        @Schema(description = "평균 이미지 크기 (bytes)", example = "1048576")
        Long averageSize
) {
    public ImageStatsResponse {
        // averageSize 계산
        if (averageSize == null && imageCount != null && imageCount > 0 && totalSize != null) {
            averageSize = totalSize / imageCount;
        }
    }
}