package backend.airo.domain.image.exception;

import java.util.List;

/**
 * 이미지를 찾을 수 없을 때 발생하는 예외
 */
public class ImageNotFoundException extends ImageException {

    private final Long imageId;
    private final List<Long> imageIds;

    public ImageNotFoundException(Long imageId) {
        super(ImageErrorCode.IMAGE_NOT_FOUND, "DOMAIN");
        this.imageId = imageId;
        this.imageIds = null;
    }

    public ImageNotFoundException(List<Long> imageIds) {
        super(ImageErrorCode.IMAGE_NOT_FOUND, "DOMAIN");
        this.imageIds = imageIds;
        this.imageId = null;
    }


}