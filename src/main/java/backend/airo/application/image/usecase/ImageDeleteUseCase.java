package backend.airo.application.image.usecase;
import backend.airo.common.exception.NullErrorCode;
import backend.airo.domain.image.command.DeleteImageCommandService;
import backend.airo.domain.image.exception.ImageErrorCode;
import backend.airo.domain.image.util.NullCheckProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageDeleteUseCase {

    private final Class<?> sourceClass = ImageDeleteUseCase.class;
    private final DeleteImageCommandService deleteImageCommandService;

    public boolean deleteImageWithAuth(Long imageId, Long userId) {

        return deleteImageCommandService.deleteById(
                NullCheckProvider.checkNotNull(imageId, NullErrorCode.IMAGE_ID_REQUIRED, sourceClass),
                NullCheckProvider.checkNotNull(userId, NullErrorCode.USER_ID_REQUIRED, sourceClass)
        );
    }

    public void deleteMultipleImages(List<Long> imageIds, Long userId) {
        deleteImageCommandService.deleteAllById(
                NullCheckProvider.checkNotNull(imageIds, NullErrorCode.IMAGE_IDS_REQUIRED, sourceClass),
                NullCheckProvider.checkNotNull(userId, NullErrorCode.USER_ID_REQUIRED, sourceClass)
        );
    }


    public void deleteImagesByPostWithAuth(Long postId, Long userId) {
        deleteImageCommandService.deleteByPostId(
                NullCheckProvider.checkNotNull(postId, NullErrorCode.POST_ID_REQUIRED, sourceClass),
                NullCheckProvider.checkNotNull(userId, NullErrorCode.USER_ID_REQUIRED, sourceClass)
        );
    }

}