package backend.airo.persistence.promotion.repository;

import backend.airo.persistence.promotion.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionJpaRepository extends JpaRepository<PromotionEntity, Long> {
    Optional<PromotionEntity> findByPostId(Long postId);
    void deleteByPostId(Long postId);
    Optional<PromotionEntity> findById(Long postId);
}