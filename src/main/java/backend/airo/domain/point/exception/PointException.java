package backend.airo.domain.point.exception;


import backend.airo.common.exception.AiroException;
import backend.airo.common.exception.BaseErrorCode;

public class PointException extends AiroException {

    private PointException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }

    public static PointException notEnoughPoint (BaseErrorCode errorCode, String sourceLayer) {
        return new PointException(errorCode, sourceLayer);
    }
}