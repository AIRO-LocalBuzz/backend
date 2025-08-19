package backend.airo.domain.promotion.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.promotion.Promotion;
import backend.airo.domain.thumbnail.Thumbnail;

import java.util.Optional;

public interface PromotionRepository extends AggregateSupport<Promotion, Long> {
    Optional<Promotion> findByPostId(Long postId);
    void deleteByPostId(Long postId);

}
