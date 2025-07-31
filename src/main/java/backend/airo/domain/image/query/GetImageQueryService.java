package backend.airo.domain.image.query;

import backend.airo.domain.image.Image;
import backend.airo.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Component
public class GetImageQueryService {
    private final ImageRepository imageRepository;

    //ToDo : id가 null인 경우 Aspect
    public Image getSingleImage(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("이미지 ID는 필수입니다");
        }
        Image image = imageRepository.findById(id);

        return image;
    }

    public boolean imageExists(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("이미지 ID는 필수입니다");
        }

        return imageRepository.existsById(id);
    }

    public Collection<Image> getImagesBelongsPost(Long postId) {
        if (postId == null) {
            throw new IllegalArgumentException("게시물 ID는 필수입니다");
        }

        return imageRepository.findImagesAllByPostId(postId);
    }

    public Page<Image> getPagedImages(Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("페이지 정보는 필수입니다");
        }

        return imageRepository.findAll(pageable);
    }

    public List<Image> getSortedImagesByPost(Long postId) {
        if (postId == null) {
            throw new IllegalArgumentException("게시물 ID는 필수입니다");
        }

        return imageRepository.findByPostIdOrderBySortOrder(postId);
    }

    public List<Image> getImagesByMimeType(String mimeType) {
        if (mimeType == null || mimeType.trim().isEmpty()) {
            throw new IllegalArgumentException("MIME 타입은 필수입니다");
        }

        return imageRepository.findByMimeType(mimeType);
    }




}
