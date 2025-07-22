package backend.airo.domain.point_history.exception;


import backend.airo.common.exception.AiroException;
import backend.airo.common.exception.BaseErrorCode;

public class PointHistoryException extends AiroException {

    private PointHistoryException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }

    public static PointHistoryException notEnoughPoint (BaseErrorCode errorCode, String sourceLayer) {
        return new PointHistoryException(errorCode, sourceLayer);
    }
}