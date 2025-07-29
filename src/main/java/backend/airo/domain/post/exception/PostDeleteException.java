package backend.airo.domain.post.exception;

/**
 * 게시물 삭제가 불가능할 때 발생하는 예외
 */
public class PostDeleteException extends PostException {

    private final Long postId;
    private final String reason;

    public PostDeleteException(Long postId, String reason) {
        super(String.format("게시물 삭제가 불가능합니다. PostID: %d, 사유: %s", postId, reason),
                "POST_DELETE_INVALID");
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
        return "게시물을 삭제할 수 없습니다. " + reason;
    }
}