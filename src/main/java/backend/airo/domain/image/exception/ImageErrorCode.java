package backend.airo.domain.image.exception;

import backend.airo.common.exception.BaseErrorCode;
import backend.airo.common.exception.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageErrorCode implements BaseErrorCode {

    IMAGE_NOT_FOUND(404, "IMAGE_001", "이미지를 찾을 수 없습니다"),
    IMAGES_NOT_FOUND(404, "IMAGE_002", "찾을 수 있는 이미지가 없습니다."),
    // 400 에러들
    IMAGE_ID_REQUIRED(400, "IMAGE_001", "잘못된 이미지 ID입니다"),

    IMAGE_INFO_REQUIRED(400, "IMAGE_002", "이미지 정보는 필수입니다"),
    IMAGE_UNSUPPORTED_FORMAT(400, "IMAGE_003", "지원하지 않는 이미지 형식입니다"),
    IMAGE_MIME_TYPE_REQUIRED(400, "IMAGE_004", "이미지 MIME 타입은 필수입니다"),
    IMAGE_URL_REQUIRED(400, "IMAGE_007", "이미지 URL은 필수입니다"),
    USER_ID_REQUIRED(400, "IMAGE_008", "잘못된 사용자 ID입니다"),
    POST_ID_REQUIRED(400, "IMAGE_009", "잘못된 게시물 ID입니다"),
    IMAGE_IDS_REQUIRED(400, "IMAGE_010", "이미지 ID 목록은 필수입니다"),
    IMAGES_REQUIRED(400, "IMAGE_011", "이미지 목록은 필수입니다"),

    // 403 에러들
    IMAGE_DELETE_UNAUTHORIZED(403, "IMAGE_101", "이미지 삭제 권한이 없습니다"),
    POST_IMAGES_DELETE_UNAUTHORIZED(403, "IMAGE_102", "게시물 이미지 삭제 권한이 없습니다"),

    // 500 에러들
    IMAGE_SAVE_FAILED(500, "IMAGE_101", "이미지 저장 중 오류가 발생했습니다"),
    IMAGE_THUMBNAIL_GENERATION_FAILED(500, "IMAGE_102", "썸네일 생성 중 오류가 발생했습니다"),
    IMAGE_DELETE_FAILED(500, "IMAGE_203", "이미지 삭제 중 오류가 발생했습니다");

    private final int status;
    private final String code;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, code, message);
    }
}