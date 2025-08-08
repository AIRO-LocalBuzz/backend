package backend.airo.domain.post.exception;
import backend.airo.common.exception.BaseErrorCode;

public class InvalidPostLikeException extends PostLikeException {

    public InvalidPostLikeException(BaseErrorCode errorCode, String field, String value) {
        super(errorCode, "DOMAIN");
    }
}