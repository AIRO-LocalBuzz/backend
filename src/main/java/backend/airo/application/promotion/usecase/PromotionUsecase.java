package backend.airo.application.promotion.usecase;

import backend.airo.application.promotion.PromotionGenerationService;
import backend.airo.cache.promotion.PromotionCacheService;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.query.GetPostQueryService;
import backend.airo.domain.promotion.Promotion;
import backend.airo.domain.promotion.PromotionResult;
import backend.airo.domain.promotion.PromotionImageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class PromotionUsecase {

    private final GetPostQueryService getPostQueryService;
    private final PromotionGenerationService promotionGenerationService;
    private final PromotionCacheService promotionCacheService; // 변경

    /**
     * 홍보물 생성 (텍스트만)
     */
    @Transactional
    public CompletableFuture<PromotionResult> generatePromotion(Long postId, Long userId) {
        Post post = getPostQueryService.handle(postId);
        return promotionGenerationService.generatePromotionAsync(post);
    }

    /**
     * 홍보물 이미지 생성
     */
    @Transactional
    public CompletableFuture<PromotionImageResult> generatePromotionWithImage(Long postId, Long userId) {
        Post post = getPostQueryService.handle(postId);
        return promotionGenerationService.generatePromotionWithImageAsync(post);
    }

    /**
     * 홍보물 조회 (캐시 적용)
     */
    @Transactional(readOnly = true)
    public Promotion getPromotion(Long postId, Long userId) {
        Post post = getPostQueryService.handle(postId);
        return promotionCacheService.getPromotion(postId); // 변경
    }

    /**
     * 홍보물 삭제
     */
    @Transactional
    public void deletePromotion(Long postId, Long userId) {
        Post post = getPostQueryService.handle(postId);
        
        // 삭제 전 캐시 정리를 위해 기존 홍보물 조회
        Promotion existingPromotion = promotionCacheService.getPromotion(postId);
        
        promotionGenerationService.deleteByPostId(postId);
        
        // 캐시 삭제
        if (existingPromotion != null) {
            promotionCacheService.evictPromotionCaches(postId, existingPromotion.getPromotionResult());
        }
    }

    /**
     * 홍보물 재생성
     */
    @Transactional
    public CompletableFuture<PromotionImageResult> regeneratePromotionWithImage(Long postId, Long userId) {
        Post post = getPostQueryService.handle(postId);
        
        // 삭제 전 캐시 정리를 위해 기존 홍보물 조회
        Promotion existingPromotion = promotionCacheService.getPromotion(postId);
        
        promotionGenerationService.deleteByPostId(postId);
        
        // 캐시 삭제
        if (existingPromotion != null) {
            promotionCacheService.evictPromotionCaches(postId, existingPromotion.getPromotionResult());
        }
        
        return promotionGenerationService.generatePromotionWithImageAsync(post);
    }
    
    /**
     * 홍보물 이미지 데이터 조회 (캐시 적용)
     */
    public byte[] getPromotionImageData(Long postId) {
        return promotionCacheService.getPromotionImageData(postId); // 변경
    }

    /**
     * taskId로 진행 상태 및 이미지 조회
     */
    public PromotionImageResult getPromotionStatusWithImage(String taskId) {
        PromotionImageResult result = promotionGenerationService.getPromotionStatus(taskId);
        return result;
    }

    /**
     * 홍보물 동기 생성 (즉시 결과 반환)
     */
    @Transactional
    public Promotion generatePromotionSync(Long postId, Long userId) {
        Post post = getPostQueryService.handle(postId);
        PromotionResult result = promotionGenerationService.generatePromotionSync(post);
        return promotionGenerationService.findByPostId(postId); // 저장된 Promotion 반환
    }
}