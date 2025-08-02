package backend.airo.persistence.thumbnail.repository;

import backend.airo.persistence.thumbnail.entity.ThumbnailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ThumbnailJpaRepository extends JpaRepository<ThumbnailEntity, Long> {
    Optional<ThumbnailEntity> findByPostId(Long postId);
    void deleteByPostId(Long postId);
}