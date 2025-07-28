package backend.airo.domain.clure_fatvl.exception;

import backend.airo.common.exception.BaseErrorCode;
import backend.airo.common.exception.ErrorReason;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ClutrFatvlErrorCode implements BaseErrorCode {

    //Test 전용
    TEST_ERROR(400, "TEST_ERROR", "Test 저장 실패"),

    ;

    private final Integer status;
    private final String errorCode;
    private final String message;

    ClutrFatvlErrorCode(int status, String errorCode, String message) {
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
