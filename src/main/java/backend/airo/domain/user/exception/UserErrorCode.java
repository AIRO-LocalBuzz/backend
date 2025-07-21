package backend.airo.domain.user.exception;

import backend.airo.common.exception.BaseErrorCode;
import backend.airo.common.exception.ErrorReason;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserErrorCode implements BaseErrorCode {
    
    NICKNAME_ALREADY_EXISTS(400, "USER_001", "이미 사용중인 닉네임입니다."),
    NICKNAME_INVALID(400, "USER_002", "유효하지 않은 닉네임입니다."),
    USER_NOT_FOUND(404, "USER_003", "사용자를 찾을 수 없습니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    UserErrorCode(int status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, errorCode, message);
    }

    public int status() {
        return status;
    }

    public String message() {
        return message;
    }
}