package backend.airo.api.promotion.dto;

import backend.airo.domain.promotion.PromotionImageResult;
import backend.airo.domain.promotion.PromotionImageStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "홍보물 이미지 응답")
public record PromotionImageResponse(
        @Schema(description = "작업 ID", example = "uuid-1234-5678")
        String taskId,

        @Schema(description = "홍보물 정보")
        PromotionResponse promotionResult,

        @Schema(description = "생성 상태", example = "COMPLETED")
        PromotionImageStatus status,

        @Schema(description = "이미지 준비 여부", example = "true")
        boolean imageReady,

        @Schema(description = "생성 시간")
        LocalDateTime createdAt,

        @Schema(description = "에러 메시지", example = "null")
        String errorMessage
) {
    public static PromotionImageResponse fromDomain(PromotionImageResult result) {
        return new PromotionImageResponse(
                result.taskId(),
                PromotionResponse.fromDomain(
                        new backend.airo.domain.promotion.Promotion(
                                null,
                                null,
                                result.promotionResult().spotName(),
                                result.promotionResult().mainImageUrl(),
                                result.promotionResult().recommendedTags(),
                                result.promotionResult().emotions(),
                                result.promotionResult().suggestedTitle(),
                                result.promotionResult().content()
                        )
                ),
                result.status(),
                result.isImageReady(),
                result.createdAt(),
                result.errorMessage()
        );
    }
}