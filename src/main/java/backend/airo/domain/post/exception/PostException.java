package backend.airo.domain.post.exception;

import backend.airo.common.exception.AiroException;
import backend.airo.common.exception.BaseErrorCode;

/**
 * 게시물 관련 비즈니스 예외의 기본 클래스
 */
public class PostException extends AiroException {

    // 기본 생성자
    public PostException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }

    // sourceLayer 없는 생성자
    public PostException(BaseErrorCode errorCode) {
        super(errorCode, null);
    }

    // 정적 팩토리 메서드들
    public static PostException notFound(BaseErrorCode errorCode, String sourceLayer) {
        return new PostException(errorCode, sourceLayer);
    }

    public static PostException notFound(BaseErrorCode errorCode) {
        return new PostException(errorCode, "DOMAIN");
    }

    public static PostException accessDenied(BaseErrorCode errorCode, String sourceLayer) {
        return new PostException(errorCode, sourceLayer);
    }

    public static PostException accessDenied(BaseErrorCode errorCode) {
        return new PostException(errorCode, "DOMAIN");
    }

    public static PostException invalidStatus(BaseErrorCode errorCode, String sourceLayer) {
        return new PostException(errorCode, sourceLayer);
    }

    public static PostException invalidStatus(BaseErrorCode errorCode) {
        return new PostException(errorCode, "DOMAIN");
    }
}