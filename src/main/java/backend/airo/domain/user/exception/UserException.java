package backend.airo.domain.user.exception;

import backend.airo.common.exception.AiroException;

public class UserException extends AiroException {
    public UserException(UserErrorCode errorCode) {
        super(errorCode, "User Layer");
    }
}