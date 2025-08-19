package backend.airo.application.promotion.usecase;

import backend.airo.api.post.dto.PostCreateRequest;
import backend.airo.application.promotion.PromotionGenerationService;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.query.GetPostQueryService;
import backend.airo.domain.post.repository.PostRepository;
import backend.airo.domain.promotion.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PromotionUsecase {
    private final GetPostQueryService getPostQueryService;
    private final PromotionGenerationService promotionGenerationService;

    @Transactional
    public void createPromotion (Long postId, Long userId) {

        Post post = getPostQueryService.handle(postId);
        promotionGenerationService.generatePromotionAsync(post);
    }
}
