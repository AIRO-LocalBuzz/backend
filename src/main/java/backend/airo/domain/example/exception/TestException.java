package backend.airo.domain.example.exception;

import backend.airo.common.exception.AiroException;
import backend.airo.common.exception.BaseErrorCode;

public class TestException extends AiroException {

    private TestException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }

    public static TestException notFound(BaseErrorCode errorCode, String sourceLayer) {
        return new TestException(errorCode, sourceLayer);
    }
}