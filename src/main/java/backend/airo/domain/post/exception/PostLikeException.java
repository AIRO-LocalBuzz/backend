package backend.airo.domain.post.exception;

import backend.airo.common.exception.AiroException;
import backend.airo.common.exception.BaseErrorCode;

/**
 * 게시물 관련 비즈니스 예외의 기본 클래스
 */
public class PostLikeException extends AiroException {

    // 기본 생성자
    public PostLikeException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }


}