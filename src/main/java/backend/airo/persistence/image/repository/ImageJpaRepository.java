package backend.airo.persistence.image.repository;

import backend.airo.persistence.image.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageJpaRepository extends JpaRepository<ImageEntity, Long> {
    Optional<ImageEntity> findByUrl(String url);
    Optional<ImageEntity> findById(Long id);
}
