package backend.airo.domain.user.exception;

import backend.airo.common.exception.BaseErrorCode;
import backend.airo.common.exception.ErrorReason;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    USER_NOT_FOUND(404, "USER_001", "사용자를 찾을 수 없습니다."),
    INVALID_FIREBASE_TOKEN(401, "USER_002", "유효하지 않은 Firebase 토큰입니다."),
    USER_ALREADY_EXISTS(409, "USER_003", "이미 존재하는 사용자입니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, errorCode, message);
    }
}