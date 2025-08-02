package backend.airo.domain.post.exception;

import backend.airo.common.exception.BaseErrorCode;

/**
 * 게시물 발행 조건을 만족하지 않을 때 발생하는 예외
 */
public class PostPublishException extends PostException {

    private final Long postId;
    private final String reason;

    public PostPublishException(Long postId, String reason, BaseErrorCode errorCode) {
        super(errorCode, "DOMAIN");
        this.postId = postId;
        this.reason = reason;
    }

    public PostPublishException(Long postId, String reason, BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
        this.postId = postId;
        this.reason = reason;
    }

    public Long getPostId() {
        return postId;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String getMessage() {
        return String.format("%s - 게시물 발행이 불가능합니다. PostID: %d, 사유: %s",
                sourceLayer, postId, reason);
    }
}