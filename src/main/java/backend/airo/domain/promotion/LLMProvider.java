package backend.airo.domain.promotion;

public interface LLMProvider {
    PromotionResult generatePromotion(PromotionRequest request);
}