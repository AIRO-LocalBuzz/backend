package backend.airo.domain.post.exception;

/**
 * 게시물 관련 비즈니스 예외의 기본 클래스
 */
public abstract class PostException extends RuntimeException {

    private final String errorCode;

    protected PostException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    protected PostException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 사용자 친화적 메시지 반환 (하위 클래스에서 구현)
     */
    public abstract String getUserMessage();
}