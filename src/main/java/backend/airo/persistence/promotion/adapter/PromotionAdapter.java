package backend.airo.persistence.promotion.adapter;

import backend.airo.domain.promotion.Promotion;
import backend.airo.domain.promotion.repository.PromotionRepository;
import backend.airo.domain.thumbnail.Thumbnail;
import backend.airo.domain.thumbnail.repository.ThumbnailRepository;
import backend.airo.persistence.promotion.PromotionEntity;
import backend.airo.persistence.promotion.repository.PromotionJpaRepository;
import backend.airo.persistence.thumbnail.entity.ThumbnailEntity;
import backend.airo.persistence.thumbnail.repository.ThumbnailJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PromotionAdapter implements PromotionRepository {

    private final PromotionJpaRepository promotionJpaRepository;

    @Override
    public Promotion save(Promotion promotion) {
        PromotionEntity entity = PromotionEntity.toEntity(promotion);
        PromotionEntity saved = promotionJpaRepository.save(entity);
        return PromotionEntity.toDomain(saved);
    }

    @Override
    public Collection<Promotion> saveAll(Collection<Promotion> aggregates) {
        return List.of();
    }

    @Override
    public Promotion findById(Long id) {
        return promotionJpaRepository.findById(id)
                .map(PromotionEntity::toDomain)
                .orElse(null);
    }

    @Override
    public Optional<Promotion> findByPostId(Long postId) {
        return promotionJpaRepository.findByPostId(postId)
                .map(PromotionEntity::toDomain);
    }

    @Override
    public void deleteByPostId(Long postId) {
        promotionJpaRepository.deleteByPostId(postId);
    }
}