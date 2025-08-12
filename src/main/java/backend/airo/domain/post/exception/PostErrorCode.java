package backend.airo.domain.post.exception;

import backend.airo.common.exception.BaseErrorCode;
import backend.airo.common.exception.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements BaseErrorCode {

    // 404 에러들
    POST_NOT_FOUND(404, "POST_001", "게시물을 찾을 수 없습니다"),

    // 403 에러들
    POST_ACCESS_DENIED(403, "POST_101", "게시물에 접근할 권한이 없습니다"),

    // 400 에러들
    POST_ALREADY_PUBLISHED(400, "POST_202", "이미 발행된 게시물입니다"),

    POST_PUBLISH_INVALID_CONDITION(400, "POST_305", "게시물 발행 조건을 만족하지 않습니다"),

    POST_CANNOT_CHANGE_STATUS(400, "POST_406", "게시물 상태를 변경할 수 없습니다");

    private final int status;
    private final String code;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, code, message);

    }
}