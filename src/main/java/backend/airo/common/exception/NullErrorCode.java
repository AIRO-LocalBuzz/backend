package backend.airo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NullErrorCode implements BaseErrorCode {

    // 400 에러들
    IMAGE_URL_REQUIRED(400, "NULL_001", "이미지 URL은 필수입니다"),
    IMAGE_INFO_REQUIRED(400, "NULL_002", "이미지 정보는 필수입니다"),
    IMAGE_MIME_TYPE_REQUIRED(400, "NULL_003", "이미지 MIME 타입은 필수입니다"),
    IMAGE_ID_REQUIRED(400, "NULL_004", "이미지 ID는 필수입니다"),
    USER_ID_REQUIRED(400, "NULL_005", "사용자 ID는 필수입니다"),
    POST_ID_REQUIRED(400, "NULL_006", "게시물 ID는 필수입니다"),
    IMAGE_IDS_REQUIRED(400, "NULL_007", "이미지 ID 목록은 필수입니다"),
    IMAGES_REQUIRED(400, "NULL_008", "이미지 목록은 필수입니다");


    private final int status;
    private final String code;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, code, message);
    }
}