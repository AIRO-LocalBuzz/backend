package backend.airo.domain.post.exception;

/**
 * 게시물 발행 조건을 만족하지 않을 때 발생하는 예외
 */
public class PostPublishException extends PostException {

    private final Long postId;
    private final String reason;

    public PostPublishException(Long postId, String reason) {
        super(String.format("게시물 발행이 불가능합니다. PostID: %d, 사유: %s", postId, reason),
                "POST_PUBLISH_INVALID");
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
    public String getUserMessage() {
        return "게시물 발행 조건을 만족하지 않습니다. " + reason;
    }
}