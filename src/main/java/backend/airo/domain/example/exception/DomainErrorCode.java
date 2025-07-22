package backend.airo.domain.example.exception;

import backend.airo.common.exception.BaseErrorCode;
import backend.airo.common.exception.ErrorReason;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DomainErrorCode implements BaseErrorCode {

    //Test 전용
    TEST_ERROR(400, "TEST_ERROR", "Test 저장 실패"),

    //Point
    NOT_ENOUGH_POINT(400, "NOT_ENOUGH_POINT", "사용자의 포인트가 부족 합니다.")
    ;

    private final Integer status;
    private final String errorCode;
    private final String message;

    DomainErrorCode(int status, String errorCode, String message) {
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
