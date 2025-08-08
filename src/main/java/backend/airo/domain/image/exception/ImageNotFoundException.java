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


    @Override
    public String getMessage() {
        if (imageId != null) {
            return String.format("%s - 이미지를 찾을 수 없습니다. ImageID: %d", sourceLayer, imageId);
        }else if (imageIds != null) {
            return String.format("%s - 찾을 수 있는 이미지가 없습니다. ImageIDs: %s", sourceLayer, imageIds);
        }else{
            return String.format("%s - 이미지 정보가 없습니다.", sourceLayer);
        }
    }

}