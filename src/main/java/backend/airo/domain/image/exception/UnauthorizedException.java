package backend.airo.domain.image.exception;

import backend.airo.common.exception.BaseErrorCode;

/**
 * 권한이 없을 때 발생하는 예외
 */
public class UnauthorizedException extends ImageException {

    private final String operation;

    public UnauthorizedException(String operation) {
        super(determineErrorCode(operation), "DOMAIN");
        this.operation = operation;
    }

    private static BaseErrorCode determineErrorCode(String operation) {
        if (operation.contains("게시물")) {
            return ImageErrorCode.POST_IMAGES_DELETE_UNAUTHORIZED;
        }
        return ImageErrorCode.IMAGE_DELETE_UNAUTHORIZED;
    }

    public String getOperation() {
        return operation;
    }

    @Override
    public String getMessage() {
        return String.format("%s - %s 권한이 없습니다", sourceLayer, operation);
    }
}