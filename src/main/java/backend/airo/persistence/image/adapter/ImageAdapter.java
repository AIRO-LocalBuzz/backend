package backend.airo.persistence.image.adapter;

import backend.airo.domain.image.Image;
import backend.airo.domain.image.exception.ImageException;
import backend.airo.domain.image.repository.ImageRepository;
import backend.airo.persistence.image.entity.ImageEntity;
import backend.airo.persistence.image.repository.ImageJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class ImageAdapter implements ImageRepository {

    private final ImageJpaRepository imageJpaRepository;

    @Override
    public Image save(Image image) {
        ImageEntity entity = ImageEntity.toEntity(image);
        ImageEntity savedEntity = imageJpaRepository.save(entity);
        return ImageEntity.toDomain(savedEntity);
    }

    @Override
    public Collection<Image> saveAll(Collection<Image> images) {
        List<ImageEntity> entities = images.stream()
                .map(ImageEntity::toEntity)
                .toList();

        List<ImageEntity> savedEntities = imageJpaRepository.saveAll(entities);

        return savedEntities.stream()
                .map(ImageEntity::toDomain)
                .toList();
    }

    @Override
    public Image findById(Long id) {
        ImageEntity imageEntity = imageJpaRepository.findById(id)
                .orElseThrow(() -> ImageException.notFound(id));
        return ImageEntity.toDomain(imageEntity);
    }

    @Override
    public boolean existsById(Long id) {
        return imageJpaRepository.existsById(id);
    }

    @Override
    public boolean existsByPostId(Long id) {
        return imageJpaRepository.existsByPostId(id);
    }

    @Override
    public Collection<Image> findImagesAllByPostId(Long postId) {
        if (!imageJpaRepository.existsByPostId(postId)) {
            throw ImageException.notFound(postId);
        }
        Collection<ImageEntity> entities = imageJpaRepository.findByPostId(postId);
        return entities.stream()
                .map(ImageEntity::toDomain)
                .toList();
    }

    @Override
    public Page<Image> findAll(Pageable pageable) {
        Page<ImageEntity> entityPage = imageJpaRepository.findAll(pageable);
        return entityPage.map(ImageEntity::toDomain);
    }

    @Override
    public List<Image> findByPostIdOrderBySortOrder(Long postId) {
        List<ImageEntity> entities = imageJpaRepository.findByPostIdOrderBySortOrder(postId);
        return entities.stream()
                .map(ImageEntity::toDomain)
                .toList();
    }


    @Override
    public List<String> findImageUrlsByPostId(Long postId) {
        return imageJpaRepository.findImageUrlsByPostId(postId);
    }

    @Override
    public List<Image> findByMimeType(String mimeType) {
        List<ImageEntity> entities = imageJpaRepository.findByMimeType(mimeType);
        return entities.stream()
                .map(ImageEntity::toDomain)
                .toList();
    }

    @Override
    public List<Image> findByIsCover(boolean isCover) {
        List<ImageEntity> entities = imageJpaRepository.findByIsCover(isCover);
        return entities.stream()
                .map(ImageEntity::toDomain)
                .toList();
    }


    @Override
    public List<Image> findAllById(List<Long> ids) {
        List<ImageEntity> entities = imageJpaRepository.findAllById(ids);
        return entities.stream()
                .map(ImageEntity::toDomain)
                .toList();
    }


    @Override
    public void deleteById(Long id) {
        if (!imageJpaRepository.existsById(id)) {
            throw ImageException.notFound(id);
        }
        imageJpaRepository.deleteById(id);
    }

    @Override
    public void deleteAllById(List<Long> ids) {
        imageJpaRepository.deleteAllById(ids);
    }

    @Override
    public void deleteByPostId(Long postId) {
        if (!imageJpaRepository.existsByPostId(postId)) {
            throw ImageException.notFound(postId);
        }
        imageJpaRepository.deleteByPostId(postId);
    }

    @Override
    public String generateThumbnail(String originalImageUrl) {
        // 썸네일 생성 로직 (실제 구현에서는 이미지 처리 서비스 호출)
        // 예시: URL에 "thumbnail-" 접두사 추가
        String fileName = originalImageUrl.substring(originalImageUrl.lastIndexOf("/") + 1);
        String basePath = originalImageUrl.substring(0, originalImageUrl.lastIndexOf("/") + 1);
        return basePath + "thumbnail-" + fileName;
    }
}