package backend.airo.domain.image.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.image.Image;
import org.aspectj.apache.bcel.Repository;

import java.util.Collection;

public interface ImageRepository extends AggregateSupport<Image, Long> {
    Image findById(Long id);
    Image deleteById(Long id);
    boolean imageExists(Long id);
    Collection<Image> findImagesAllByPostId(Long postId);
    void deleteImagesAllByPostId(Long postId);


}
