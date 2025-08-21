package backend.airo.domain.promotion;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * 홍보물 생성 결과 (텍스트 콘텐츠 + 합성 이미지)
 */
public record PromotionImageResult(
        PromotionResult promotionResult,
        CompletableFuture<byte[]> imageData,
        String taskId,
        PromotionImageStatus status,
        LocalDateTime createdAt,
        String errorMessage
) {

    /**
     * LLM 응답만 있는 초기 상태
     */
    public static PromotionImageResult withTextOnly(PromotionResult promotionResult, String taskId) {
        return new PromotionImageResult(
                promotionResult,
                null,
                taskId,
                PromotionImageStatus.PROCESSING,
                LocalDateTime.now(),
                null
        );
    }

    /**
     * 이미지 생성 완료 상태
     */
    public static PromotionImageResult withImage(PromotionResult promotionResult,
                                                 CompletableFuture<byte[]> imageData,
                                                 String taskId) {
        return new PromotionImageResult(
                promotionResult,
                imageData,
                taskId,
                PromotionImageStatus.COMPLETED,
                LocalDateTime.now(),
                null
        );
    }

    /**
     * 실패 상태
     */
    public static PromotionImageResult withError(PromotionResult promotionResult,
                                                 String taskId,
                                                 String errorMessage) {
        return new PromotionImageResult(
                promotionResult,
                null,
                taskId,
                PromotionImageStatus.FAILED,
                LocalDateTime.now(),
                errorMessage
        );
    }

    /**
     * 이미지 생성 준비 완료 여부
     */
    public boolean isImageReady() {
        return imageData != null && imageData.isDone() && !imageData.isCompletedExceptionally();
    }
}