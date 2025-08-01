package backend.airo.domain.post.exception;

import backend.airo.common.exception.BaseErrorCode;

/**
 * 게시물을 찾을 수 없을 때 발생하는 예외
 */
public class PostNotFoundException extends PostException {

    private final Long postId;

    public PostNotFoundException(Long postId, BaseErrorCode errorCode) {
        super(errorCode, "DOMAIN");
        this.postId = postId;
    }

    public PostNotFoundException(Long postId, BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }

    @Override
    public String getMessage() {
        return String.format("%s - 게시물을 찾을 수 없습니다. PostID: %d", sourceLayer, postId);
    }
}