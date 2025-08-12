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
    POST_AUTHOR_NOT_FOUND(404, "POST_002", "게시물 작성자를 찾을 수 없습니다"),
    POST_LIKE_NOT_FOUND(404, "POST_LIKE_101", "좋아요를 찾을 수 없습니다"),
    POST_REQUEST_REQUIRED(400, "POST_001", "게시물 요청은 필수입니다"),
    POST_CONTENT_REQUIRED(400, "POST_002", "게시물 내용은 필수입니다"),
    POST_STATUS_REQUIRED(400, "POST_003", "게시물 상태는 필수입니다"),

    // 403 에러들
    POST_ACCESS_DENIED(403, "POST_101", "게시물에 접근할 권한이 없습니다"),
    POST_EDIT_DENIED(403, "POST_102", "게시물을 수정할 권한이 없습니다"),
    POST_DELETE_DENIED(403, "POST_103", "게시물을 삭제할 권한이 없습니다"),

    // 400 에러들
    POST_INVALID_STATUS(400, "POST_201", "유효하지 않은 게시물 상태입니다"),
    POST_ALREADY_PUBLISHED(400, "POST_202", "이미 발행된 게시물입니다"),
    POST_CONTENT_TOO_LONG(400, "POST_203", "게시물 내용이 너무 깁니다"),
    POST_TITLE_REQUIRED(400, "POST_204", "게시물 제목은 필수입니다"),

    // 발행 관련 에러들
    POST_PUBLISH_TITLE_REQUIRED(400, "POST_301", "제목은 발행을 위한 필수 항목입니다"),
    POST_PUBLISH_CONTENT_REQUIRED(400, "POST_302", "내용은 발행을 위한 필수 항목입니다"),
    POST_PUBLISH_CATEGORY_REQUIRED(400, "POST_303", "카테고리는 발행을 위한 필수 항목입니다"),
    POST_PUBLISH_LOCATION_REQUIRED(400, "POST_304", "위치는 발행을 위한 필수 항목입니다"),
    POST_PUBLISH_INVALID_CONDITION(400, "POST_305", "게시물 발행 조건을 만족하지 않습니다"),
    POST_PUBLISH_DRAFT_ONLY(400, "POST_306", "임시저장 상태의 게시물만 발행할 수 있습니다"),

    POST_CANNOT_DRAFT_PUBLISHED(400, "POST_402", "발행된 게시물은 임시저장으로 변경할 수 없습니다"),
    POST_CANNOT_DELETE_PUBLISHED(400, "POST_403", "발행된 게시물은 바로 삭제할 수 없습니다"),
    POST_CANNOT_PUBLISH_ARCHIVED(400, "POST_404", "보관된 게시물은 발행할 수 없습니다"),
    POST_ALREADY_IN_STATUS(400, "POST_405", "이미 해당 상태입니다"),
    POST_CANNOT_CHANGE_STATUS(400, "POST_406", "게시물 상태를 변경할 수 없습니다"),

    // 409 에러들
    POST_LIKE_ALREADY_EXISTS(409, "POST_LIKE_201", "이미 좋아요가 존재합니다");

    private final int status;
    private final String code;
    private final String message;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status, code, message);

    }
}