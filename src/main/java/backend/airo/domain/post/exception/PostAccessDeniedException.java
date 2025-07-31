package backend.airo.domain.post.exception;

import backend.airo.common.exception.BaseErrorCode;

/**
 * 게시물 액세스 권한이 없을 때 발생하는 예외
 */
public class PostAccessDeniedException extends PostException {

    private final Long postId;
    private final Long userId;

    public PostAccessDeniedException(Long postId, Long userId, BaseErrorCode errorCode) {
        super(errorCode, "DOMAIN");
        this.postId = postId;
        this.userId = userId;
    }

    public PostAccessDeniedException(Long postId, Long userId, BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
        this.postId = postId;
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String getMessage() {
        return String.format("%s - 게시물에 대한 접근 권한이 없습니다. PostID: %d, UserID: %d",
                sourceLayer, postId, userId);
    }
}