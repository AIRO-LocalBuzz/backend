package backend.airo.domain.image.exception;

import backend.airo.common.exception.BaseErrorCode;

/**
 * 이미지 정보가 유효하지 않을 때 발생하는 예외
 */
public class InvalidImageException extends ImageException {

    private final String field;
    private final String value;

    public InvalidImageException(BaseErrorCode errorCode, String field, String value) {
        super(errorCode, "DOMAIN");
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getMessage() {
        return String.format("%s (필드: %s, 값: %s)",
                super.getErrorCode().getErrorReason().message(), field, value);
    }
}