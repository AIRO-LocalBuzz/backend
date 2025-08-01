package backend.airo.application.image.usecase;
import backend.airo.domain.image.Image;
import backend.airo.domain.image.command.UpdateImageCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageUpdateUseCase {

    private final UpdateImageCommandService updateImageCommandService;


    public Image updateImageSortOrder(Long imageId, Integer newSortOrder) {
        return updateImageCommandService.updateSortOrder(imageId, newSortOrder);
    }


    public Image updateImageCaption(Long imageId, String newCaption) {
        return updateImageCommandService.updateCaption(imageId, newCaption);
    }


    public Image updateImageAltText(Long imageId, String newAltText) {
        return updateImageCommandService.updateAltText(imageId, newAltText);
    }


    public List<Image> reorderImages(List<Long> imageIds) {
        Collection<Image> result = updateImageCommandService.reorderImages(imageIds);
        return result instanceof List ? (List<Image>) result : new ArrayList<>(result);
    }


    public List<Image> updateMultipleImages(List<Image> images) {
        return updateImageCommandService.updateMultipleImages(images);
    }

}