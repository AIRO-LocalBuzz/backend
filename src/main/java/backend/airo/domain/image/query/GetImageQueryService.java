package backend.airo.domain.image.query;

import backend.airo.domain.image.Image;
import backend.airo.domain.image.repository.ImageRepository;
import backend.airo.domain.post.exception.PostErrorCode;
import backend.airo.domain.post.exception.PostNotFoundException;
import backend.airo.domain.post.repository.PostRepository;
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
    private final PostRepository postRepository;

    public Image getSingleImage(Long id) {
        return imageRepository.findById(id);
    }

    public Collection<Image> getImagesBelongsPost(Long postId) {
        return imageRepository.findImagesAllByPostId(postId);
    }

    public Page<Image> getPagedImages(Pageable pageable) {
        return imageRepository.findAll(pageable);
    }

    public List<Image> getSortedImagesByPost(Long postId) {
        if(!postRepository.existsById(postId)) {
            throw new PostNotFoundException(postId, PostErrorCode.POST_NOT_FOUND);
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
