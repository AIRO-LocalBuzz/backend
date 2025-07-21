package backend.airo.domain.user.exception;

import backend.airo.common.exception.AiroException;
import backend.airo.common.exception.BaseErrorCode;

public class UserException extends AiroException {

    private UserException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }

    public static UserException nicknameAlreadyExists(String sourceLayer) {
        return new UserException(UserErrorCode.NICKNAME_ALREADY_EXISTS, sourceLayer);
    }

    public static UserException nicknameInvalid(String sourceLayer) {
        return new UserException(UserErrorCode.NICKNAME_INVALID, sourceLayer);
    }

    public static UserException userNotFound(String sourceLayer) {
        return new UserException(UserErrorCode.USER_NOT_FOUND, sourceLayer);
    }
}