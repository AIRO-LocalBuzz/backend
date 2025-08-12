package backend.airo.application.image.usecase;
import backend.airo.domain.image.Image;
import backend.airo.domain.image.query.GetImageQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageReadUseCase {

    private final GetImageQueryService getImageQueryService;


    public Image getSingleImage(Long imageId) {
        return getImageQueryService.getSingleImage(imageId);
    }


    public Page<Image> getPagedImages(Pageable pageable) {
        return getImageQueryService.getPagedImages(pageable);
    }


    public List<Image> getSortedImagesByPost(Long postId) {
        return getImageQueryService.getSortedImagesByPost(postId);
    }

}