package backend.airo.domain.promotion;

import backend.airo.domain.promotion.PromotionRequest;
import backend.airo.domain.promotion.PromotionImageResult;

/**
 * 이미지 생성 기능이 포함된 LLM Provider
 */
public interface LLMProviderWithImage extends LLMProvider {

    /**
     * 텍스트 콘텐츠 + 합성 이미지 생성
     */
    PromotionImageResult generatePromotionWithImage(PromotionRequest request);

    /**
     * 진행 상태 조회
     */
    PromotionImageResult getPromotionStatus(String taskId);
}