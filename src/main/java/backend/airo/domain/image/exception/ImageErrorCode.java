package backend.airo.domain.image.exception;

import backend.airo.common.exception.BaseErrorCode;
import backend.airo.common.exception.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageErrorCode implements BaseErrorCode {

    IMAGE_NOT_FOUND(404, "IMAGE_001", "이미지를 찾을 수 없습니다"),
    IMAGE_UNSUPPORTED_FORMAT(400, "IMAGE_003", "지원하지 않는 이미지 형식입니다"),
    // 403 에러들
    IMAGE_DELETE_UNAUTHORIZED(403, "IMAGE_101", "이미지 삭제 권한이 없습니다"),
    POST_IMAGES_DELETE_UNAUTHORIZED(403, "IMAGE_102", "게시물 이미지 삭제 권한이 없습니다");


    private final int status;
    private final String code;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, code, message);
    }
}