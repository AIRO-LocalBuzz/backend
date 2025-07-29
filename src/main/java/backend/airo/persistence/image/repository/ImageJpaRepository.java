package backend.airo.persistence.image.repository;

import backend.airo.domain.image.Image;
import backend.airo.persistence.image.entity.ImageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ImageJpaRepository extends JpaRepository<ImageEntity, Long> {

    Optional<ImageEntity> findById(Long id);

    // Read
    Collection<ImageEntity> findByPostId(Long postId);
    Page<ImageEntity> findAll(Pageable pageable);
    List<ImageEntity> findByPostIdOrderBySortOrder(Long postId);
    List<ImageEntity> findByMimeType(String mimeType);
    List<ImageEntity> findByIsCover(boolean isCover);


    // Delete
    void deleteByPostId(Long postId);


}
