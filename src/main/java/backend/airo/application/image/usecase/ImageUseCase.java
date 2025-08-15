package backend.airo.application.image.usecase;
import backend.airo.domain.image.Image;
import backend.airo.domain.image.command.CreateImageCommandService;
import backend.airo.domain.image.command.DeleteImageCommandService;
import backend.airo.domain.image.command.UpdateImageCommandService;
import backend.airo.domain.image.query.GetImageQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageUseCase {

    private final CreateImageCommandService createImageCommandService;
    private final UpdateImageCommandService updateImageCommandService;
    private final DeleteImageCommandService deleteImageCommandService;
    private final GetImageQueryService getImageQueryService;

    public Image uploadSingleImage(Image image) {
        return createImageCommandService.handle(image);
    }

    public List<Image> uploadMultipleImages(List<Image> images) {
        return images.stream()
                .map(createImageCommandService::handle)
                .toList();
    }

    public Image getSingleImage(Long imageId) {
        return getImageQueryService.getSingleImage(imageId);
    }


    public Page<Image> getPagedImages(Pageable pageable) {
        return getImageQueryService.getPagedImages(pageable);
    }


    public List<Image> getSortedImagesByPost(Long postId) {
        return getImageQueryService.getSortedImagesByPost(postId);
    }



    public List<Image> reorderImages(List<Long> imageIds) {
        Collection<Image> result = updateImageCommandService.reorderImages(imageIds);
        return result instanceof List ? (List<Image>) result : new ArrayList<>(result);
    }


    public void deleteImageWithAuth(Long imageId, Long userId) {
        deleteImageCommandService.deleteById(imageId, userId);
    }

    public void deleteMultipleImages(List<Long> imageIds, Long userId) {
        deleteImageCommandService.deleteAllById(imageIds, userId);
    }

    public void deleteImagesByPostWithAuth(Long postId, Long userId) {
        deleteImageCommandService.deleteByPostId(postId, userId);
    }

}

