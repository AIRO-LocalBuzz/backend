package backend.airo.application.promotion;

import backend.airo.domain.post.Post;
import backend.airo.domain.promotion.Promotion;
import backend.airo.domain.promotion.PromotionRequest;
import backend.airo.domain.promotion.repository.PromotionRepository;
import backend.airo.domain.thumbnail.LLMProvider;
import backend.airo.domain.thumbnail.Thumbnail;
import backend.airo.domain.thumbnail.ThumbnailRequest;
import backend.airo.domain.promotion.PromotionResult;
import backend.airo.domain.image.repository.ImageRepository;
import backend.airo.domain.thumbnail.repository.ThumbnailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionGenerationService {

    private final LLMProvider llmProvider;
    private final ImageRepository imageRepository;
    private final PromotionRepository promotionRepository;

    public void generatePromotionAsync(Post post) {
        CompletableFuture.runAsync(() -> {
            try {
                log.info("썸네일 생성 시작: postId={}", post.getId());

                List<String> imageUrls = imageRepository.findImageUrlsByPostId(post.getId());
                PromotionRequest request = PromotionRequest.from(post, imageUrls);
                PromotionResult result = llmProvider.generatePromotion(request);

                savePromotion(post.getId(), result);

                log.info("썸네일 생성 완료: postId={}", post.getId());
            } catch (Exception e) {
                log.error("썸네일 생성 실패: postId={}", post.getId(), e);
            }
        });
    }

    private void savePromotion(Long postId, PromotionResult result) {
        Promotion promotion = Promotion.create(postId, result);
        promotionRepository.save(promotion);
        log.debug("썸네일 저장 완료: postId={}, result={}", postId, result);
    }
}
