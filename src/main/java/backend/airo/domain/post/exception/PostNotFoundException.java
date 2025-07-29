package backend.airo.domain.post.exception;

/**
 * 게시물을 찾을 수 없을 때 발생하는 예외
 * Domain Layer의 비즈니스 예외
 */
public class PostNotFoundException extends RuntimeException {

    private final Long postId;

    public PostNotFoundException(Long postId) {
        super(String.format("게시물을 찾을 수 없습니다. ID: %d", postId));
        this.postId = postId;
    }

    public PostNotFoundException(Long postId, String message) {
        super(message);
        this.postId = postId;
    }

    public PostNotFoundException(Long postId, String message, Throwable cause) {
        super(message, cause);
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }

    /**
     * 에러 코드 반환
     */
    public String getErrorCode() {
        return "POST_NOT_FOUND";
    }

    /**
     * 사용자 친화적 메시지 반환
     */
    public String getUserMessage() {
        return "요청하신 게시물을 찾을 수 없습니다.";
    }
}