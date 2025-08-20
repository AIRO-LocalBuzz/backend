package backend.airo.application.promotion;

import backend.airo.domain.image.repository.ImageRepository;
import backend.airo.domain.post.Post;
import backend.airo.domain.promotion.LLMProvider;
import backend.airo.domain.promotion.LLMProviderWithImage;
import backend.airo.domain.promotion.Promotion;
import backend.airo.domain.promotion.PromotionImageResult;
import backend.airo.domain.promotion.PromotionRequest;
import backend.airo.domain.promotion.PromotionResult;
import backend.airo.domain.promotion.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionGenerationService {

    private final LLMProvider llmProvider;
    private final LLMProviderWithImage llmProviderWithImage;
    private final ImageRepository imageRepository;
    private final PromotionRepository promotionRepository;

    /**
     * 기존 방식: 텍스트만 생성 (하위 호환성 유지)
     */
    @Async("apiTaskExecutor")
    public CompletableFuture<PromotionResult> generatePromotionAsync(Post post) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("홍보물 생성 시작: postId={}", post.getId());

                List<String> imageUrls = imageRepository.findImageUrlsByPostId(post.getId());
                PromotionRequest request = PromotionRequest.from(post, imageUrls);
                PromotionResult result = llmProvider.generatePromotion(request);

                savePromotionSync(post.getId(), result);

                log.info("홍보물 생성 완료: postId={}", post.getId());
                return result;

            } catch (Exception e) {
                log.error("홍보물 생성 실패: postId={}", post.getId(), e);
                throw new RuntimeException("홍보물 생성 실패: " + post.getId(), e);
            }
        });
    }

    /**
     * 새로운 방식: 텍스트 + 이미지 생성
     */
    @Async("apiTaskExecutor")
    public CompletableFuture<PromotionImageResult> generatePromotionWithImageAsync(Post post) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("홍보물 이미지 생성 시작: postId={}", post.getId());

                List<String> imageUrls = imageRepository.findImageUrlsByPostId(post.getId());
                PromotionRequest request = PromotionRequest.from(post, imageUrls);

                // Phase 3에서 구현한 이미지 생성 기능 활용
                PromotionImageResult result = llmProviderWithImage.generatePromotionWithImage(request);

                // 텍스트 콘텐츠는 즉시 저장
                savePromotionSync(post.getId(), result.promotionResult());

                log.info("홍보물 이미지 생성 완료: postId={}, taskId={}", post.getId(), result.taskId());
                return result;

            } catch (Exception e) {
                log.error("홍보물 이미지 생성 실패: postId={}", post.getId(), e);
                throw new RuntimeException("홍보물 이미지 생성 실패: " + post.getId(), e);
            }
        });
    }

    /**
     * 동기식 텍스트 생성 (즉시 결과 필요 시)
     */
    @Transactional
    public PromotionResult generatePromotionSync(Post post) {
        try {
            log.info("홍보물 동기 생성 시작: postId={}", post.getId());

            List<String> imageUrls = imageRepository.findImageUrlsByPostId(post.getId());
            PromotionRequest request = PromotionRequest.from(post, imageUrls);
            PromotionResult result = llmProvider.generatePromotion(request);

            savePromotion(post.getId(), result);

            log.info("홍보물 동기 생성 완료: postId={}", post.getId());
            return result;

        } catch (Exception e) {
            log.error("홍보물 동기 생성 실패: postId={}", post.getId(), e);
            throw new RuntimeException("홍보물 동기 생성 실패: " + post.getId(), e);
        }
    }

    /**
     * 진행 상태 조회
     */
    public PromotionImageResult getPromotionStatus(String taskId) {
        return llmProviderWithImage.getPromotionStatus(taskId);
    }

    /**
     * 기존 홍보물 조회
     */
    @Transactional(readOnly = true)
    public Promotion findByPostId(Long postId) {
        return promotionRepository.findByPostId(postId)
                .orElse(null);
    }

    /**
     * 홍보물 삭제
     */
    @Transactional
    public void deleteByPostId(Long postId) {
        try {
            promotionRepository.deleteByPostId(postId);
            log.info("홍보물 삭제 완료: postId={}", postId);
        } catch (Exception e) {
            log.error("홍보물 삭제 실패: postId={}", postId, e);
            throw new RuntimeException("홍보물 삭제 실패: " + postId, e);
        }
    }

    /**
     * 홍보물 저장 (트랜잭션 내부용)
     */
    private void savePromotion(Long postId, PromotionResult result) {
        try {
            // 기존 홍보물이 있으면 삭제
            promotionRepository.findByPostId(postId)
                    .ifPresent(existing -> promotionRepository.deleteByPostId(postId));

            // 새 홍보물 저장
            Promotion promotion = Promotion.create(postId, result);
            promotionRepository.save(promotion);

            log.debug("홍보물 저장 완료: postId={}, spotName={}", postId, result.spotName());

        } catch (Exception e) {
            log.error("홍보물 저장 실패: postId={}", postId, e);
            throw new RuntimeException("홍보물 저장 실패: " + postId, e);
        }
    }

    /**
     * 홍보물 저장 (새로운 트랜잭션으로 실행)
     */
    @Transactional
    public void savePromotionSync(Long postId, PromotionResult result) {
        savePromotion(postId, result);
    }
}