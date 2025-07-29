package backend.airo.domain.post.exception;

/**
 * 게시물 액세스 권한이 없을 때 발생하는 예외
 */
public class PostAccessDeniedException extends PostException {

    private final Long postId;
    private final Long userId;

    public PostAccessDeniedException(Long postId, Long userId) {
        super(String.format("게시물에 대한 접근 권한이 없습니다. PostID: %d, UserID: %d", postId, userId),
                "POST_ACCESS_DENIED");
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
    public String getUserMessage() {
        return "이 게시물에 접근할 권한이 없습니다.";
    }
}