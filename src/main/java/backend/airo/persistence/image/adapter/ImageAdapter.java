package backend.airo.persistence.image.adapter;

import backend.airo.domain.image.Image;
import backend.airo.domain.image.repository.ImageRepository;
import backend.airo.persistence.image.entity.ImageEntity;
import backend.airo.persistence.image.repository.ImageJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Transactional
@RequiredArgsConstructor
public class ImageAdapter implements ImageRepository {

    private final ImageJpaRepository imageJpaRepository;

    @Override
    public Image save(Image image) {
        return null;
    }

    @Override
    public Image findById(Long id) {
        ImageEntity imageEntity = imageJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post Not Found with id - " + id));
        return imageEntity.toDomain(imageEntity);
    }

    @Override
    public Collection<Image> saveAll(Collection<Image> Images) {
        return null;
    }


    @Override
    public Image deleteById(Long id) {
        return null;
    }

    @Override
    public boolean imageExists(Long id) {
        // Logic to check if an image exists by ID
        // This could involve checking the database for the existence of the entity
        // For now, we will just return true as a placeholder
        return true;
    }


    @Override
    public Collection<Image> findImagesAllByPostId(Long postId) {
        return null;
    }

    @Override
    public void deleteImagesAllByPostId(Long postId) {
        return;
    }


}
