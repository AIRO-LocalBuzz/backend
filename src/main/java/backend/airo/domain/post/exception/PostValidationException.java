package backend.airo.domain.post.exception;

import backend.airo.common.exception.BaseErrorCode;

public class PostValidationException extends PostException {

    private final String field;
    private final String value;

    public PostValidationException(BaseErrorCode errorCode, String field, String value) {
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
