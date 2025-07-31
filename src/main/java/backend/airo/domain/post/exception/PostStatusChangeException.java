package backend.airo.domain.post.exception;

import backend.airo.common.exception.BaseErrorCode;
import backend.airo.domain.post.enums.PostStatus;

/**
 * 게시물 상태 관련 예외
 */
public class PostStatusChangeException extends PostException {

    private final Long postId;
    private final PostStatus currentStatus;
    private final PostStatus targetStatus;

    public PostStatusChangeException(Long postId, PostStatus currentStatus, PostStatus targetStatus, BaseErrorCode errorCode) {
        super(errorCode, "DOMAIN");
        this.postId = postId;
        this.currentStatus = currentStatus;
        this.targetStatus = targetStatus;
    }

    public PostStatusChangeException(Long postId, PostStatus currentStatus, PostStatus targetStatus,
                               BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
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
    public String getMessage() {
        return String.format("%s - 잘못된 게시물 상태 변경입니다. PostID: %d, 현재상태: %s, 변경시도상태: %s",
                sourceLayer, postId, currentStatus, targetStatus);
    }
}