package backend.airo.domain.user.exception;

import backend.airo.common.exception.AiroException;
import backend.airo.common.exception.BaseErrorCode;

public class UserException extends AiroException {

    public UserException(BaseErrorCode baseErrorCode, String point) {
        super(baseErrorCode, point);
    }

    public static UserException notFound(BaseErrorCode baseErrorCode, String point) {
        return new UserException(baseErrorCode, point);
    }

    public static UserException alreadyExists(BaseErrorCode baseErrorCode, String point) {
        return new UserException(baseErrorCode, point);
    }

    public static UserException invalidData(BaseErrorCode baseErrorCode, String point) {
        return new UserException(baseErrorCode, point);
    }
}