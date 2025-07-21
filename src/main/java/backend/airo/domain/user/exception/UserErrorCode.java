package backend.airo.domain.user.exception;

import backend.airo.common.exception.BaseErrorCode;
import backend.airo.common.exception.ErrorReason;

public enum UserErrorCode implements BaseErrorCode {
    USER_PROFILE_NOT_FOUND(404, "USER_001", "사용자 프로필을 찾을 수 없습니다."),
    USER_PROFILE_ALREADY_EXISTS(409, "USER_002", "사용자 프로필이 이미 존재합니다."),
    INVALID_USER_DATA(400, "USER_003", "잘못된 사용자 데이터입니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    UserErrorCode(Integer status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, errorCode, message);
    }
}