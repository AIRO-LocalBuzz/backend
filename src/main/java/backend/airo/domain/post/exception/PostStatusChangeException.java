package backend.airo.domain.post.exception;

import backend.airo.domain.post.enums.PostStatus;

/**
 * 게시물 상태 변경이 불가능할 때 발생하는 예외
 */
public class PostStatusChangeException extends PostException {

    private final Long postId;
    private final PostStatus currentStatus;
    private final PostStatus targetStatus;

    public PostStatusChangeException(Long postId, PostStatus currentStatus, PostStatus targetStatus) {
        super(String.format("게시물 상태 변경이 불가능합니다. PostID: %d, 현재상태: %s, 변경하려는상태: %s",
                        postId, currentStatus, targetStatus),
                "POST_STATUS_CHANGE_INVALID");
        this.postId = postId;
        this.currentStatus = currentStatus;
        this.targetStatus = targetStatus;
    }

    public Long getPostId() {
        return postId;
    }

    public PostStatus getCurrentStatus() {
        return currentStatus;
    }

    public PostStatus getTargetStatus() {
        return targetStatus;
    }

    @Override
    public String getUserMessage() {
        return String.format("게시물을 %s 상태로 변경할 수 없습니다.", getStatusDisplayName(targetStatus));
    }

    private String getStatusDisplayName(PostStatus status) {
        return switch (status) {
            case DRAFT -> "임시저장";
            case PUBLISHED -> "발행";
            case ARCHIVED -> "보관";
        };
    }
}