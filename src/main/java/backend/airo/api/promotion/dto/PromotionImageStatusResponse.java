package backend.airo.api.promotion.dto;

import backend.airo.domain.promotion.PromotionImageResult;
import backend.airo.domain.promotion.PromotionImageStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "홍보물 이미지 상태 응답")
public record PromotionImageStatusResponse(
        @Schema(description = "작업 ID", example = "uuid-1234-5678")
        String taskId,

        @Schema(description = "생성 상태", example = "PROCESSING")
        PromotionImageStatus status,

        @Schema(description = "이미지 준비 여부", example = "false")
        boolean imageReady,

        @Schema(description = "진행률 (%)", example = "75")
        int progress,

        @Schema(description = "에러 메시지", example = "null")
        String errorMessage
) {
    public static PromotionImageStatusResponse fromDomain(PromotionImageResult result) {
        if (result == null) {
            return null;
        }

        int progress = switch (result.status()) {
            case PROCESSING -> result.isImageReady() ? 80 : 50;
            case COMPLETED -> 100;
            case FAILED -> 0;
        };

        return new PromotionImageStatusResponse(
                result.taskId(),
                result.status(),
                result.isImageReady(),
                progress,
                result.errorMessage()
        );
    }
}