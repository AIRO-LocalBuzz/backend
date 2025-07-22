package backend.airo.domain.content.exception;

import backend.airo.common.exception.AiroException;
import backend.airo.common.exception.BaseErrorCode;

public class ContentException extends AiroException {

    private ContentException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }

    public static ContentException notFound(BaseErrorCode errorCode, String sourceLayer) {
        return new ContentException(errorCode, sourceLayer);
    }
}