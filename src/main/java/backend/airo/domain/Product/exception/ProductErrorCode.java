package backend.airo.domain.Product.exception;

import backend.airo.common.exception.BaseErrorCode;
import backend.airo.common.exception.ErrorReason;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductErrorCode implements BaseErrorCode {

    ;

    private final Integer status;
    private final String errorCode;
    private final String message;

    ProductErrorCode(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, errorCode, message);
    }

    public int status() {
        return status;
    }

    public String message() {
        return message;
    }
}
