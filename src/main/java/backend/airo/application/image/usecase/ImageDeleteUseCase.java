package backend.airo.application.image.usecase;
import backend.airo.domain.image.command.DeleteImageCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageDeleteUseCase {

    private final DeleteImageCommandService deleteImageCommandService;

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