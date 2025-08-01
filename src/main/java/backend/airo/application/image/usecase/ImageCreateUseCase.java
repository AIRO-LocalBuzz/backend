package backend.airo.application.image.usecase;
import backend.airo.domain.image.Image;
import backend.airo.domain.image.command.CreateImageCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageCreateUseCase {

    private final CreateImageCommandService createImageCommandService;


    public Image uploadSingleImage(Image image) {
        return createImageCommandService.handle(image);
    }


    public List<Image> uploadMultipleImages(List<Image> images) {
        return images.stream()
                .map(createImageCommandService::handle)
                .toList();
    }


    public Image uploadImageWithRetry(Image image) {
        return createImageCommandService.handleWithRetry(image);
    }


    public Image uploadImageWithLock(Image image) {
        return createImageCommandService.handleWithLock(image);
    }


    public String generateThumbnail(String originalImageUrl) {
        return createImageCommandService.generateThumbnail(originalImageUrl);
    }

}