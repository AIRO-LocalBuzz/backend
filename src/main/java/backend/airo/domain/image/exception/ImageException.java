package backend.airo.domain.image.exception;

import backend.airo.common.exception.AiroException;
import backend.airo.common.exception.BaseErrorCode;

public class ImageException extends AiroException {
    public ImageException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }
}
