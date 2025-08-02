package backend.airo.application.image.usecase;
import backend.airo.domain.image.command.DeleteImageCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageDeleteUseCase {


    private final DeleteImageCommandService deleteImageCommandService;


    public boolean deleteSingleImage(Long imageId) {
        return deleteImageCommandService.deleteById(imageId);
    }


    public boolean deleteImageWithAuth(Long imageId, Long currentUserId) {
        return deleteImageCommandService.deleteById(imageId, currentUserId);
    }

    public void deleteMultipleImages(List<Long> imageIds) {
        deleteImageCommandService.deleteAllById(imageIds);
    }


    public void deleteImagesByPost(Long postId) {
        deleteImageCommandService.deleteByPostId(postId);
    }


    public void deleteImagesByPostWithAuth(Long postId, Long currentUserId) {
        deleteImageCommandService.deleteByPostId(postId, currentUserId);
    }


}