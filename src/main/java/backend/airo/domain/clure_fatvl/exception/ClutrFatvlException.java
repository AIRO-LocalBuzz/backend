package backend.airo.domain.clure_fatvl.exception;

import backend.airo.common.exception.AiroException;
import backend.airo.common.exception.BaseErrorCode;

public class ClutrFatvlException extends AiroException {

    private ClutrFatvlException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }

    public static ClutrFatvlException notFound(BaseErrorCode errorCode, String sourceLayer) {
        return new ClutrFatvlException(errorCode, sourceLayer);
    }
}