package backend.airo.domain.image.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.image.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface ImageRepository extends AggregateSupport<Image, Long> {

    // Read
    boolean existsById(Long id);
    Collection<Image> findImagesAllByPostId(Long postId);
    Page<Image> findAll(Pageable pageable);
    List<Image> findByPostIdOrderBySortOrder(Long postId);
    List<Image> findByMimeType(String mimeType);
    List<Image> findByIsCover(boolean isCover);
    Collection<Image> findAllById(List<Long> ids);
    List<String> findImageUrlsByPostId(Long postId);

    // Delete
    void deleteById(Long id);
    void deleteAllById(List<Long> ids);
    void deleteByPostId(Long postId);

    // Business Logic
    String generateThumbnail(String originalImageUrl);

    boolean existsByPostId(Long postId);
}
