package backend.airo.domain.image.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedException() {
        super("권한이 없습니다");
    }

    public UnauthorizedException(String action, Long resourceId) {
        super(String.format("%s 권한이 없습니다. 리소스 ID: %d", action, resourceId));
    }
}