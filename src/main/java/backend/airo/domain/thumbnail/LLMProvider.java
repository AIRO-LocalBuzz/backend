// 2. LLMProvider 인터페이스
package backend.airo.domain.thumbnail;

import backend.airo.domain.promotion.PromotionRequest;
import backend.airo.domain.promotion.PromotionResult;

public interface LLMProvider {
    PromotionResult generatePromotion(PromotionRequest request);
}