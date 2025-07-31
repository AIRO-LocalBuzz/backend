package backend.airo.domain.post.command;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * 게시물 삭제 커맨드
 */
public record DeletePostCommand(
        @NotNull(message = "게시물 ID는 필수입니다")
        @Positive(message = "게시물 ID는 양수여야 합니다")
        Long postId,

        @NotNull(message = "요청자 ID는 필수입니다")
        @Positive(message = "요청자 ID는 양수여야 합니다")
        Long requesterId,

        String deleteReason,

        Boolean forceDelete
) {

    public DeletePostCommand {
        if (deleteReason == null) {
            deleteReason = "사용자 요청에 의한 삭제";
        }
        if (forceDelete == null) {
            forceDelete = false;
        }
    }

    /**
     * 일반 삭제 커맨드 생성
     */
    public static DeletePostCommand of(Long postId, Long requesterId) {
        return new DeletePostCommand(postId, requesterId, null, false);
    }

    /**
     * 강제 삭제 커맨드 생성 (관리자용)
     */
    public static DeletePostCommand forceDelete(Long postId, Long requesterId, String reason) {
        return new DeletePostCommand(postId, requesterId, reason, true);
    }

    /**
     * 사용자 삭제 요청
     */
    public static DeletePostCommand byUser(Long postId, Long requesterId, String reason) {
        return new DeletePostCommand(postId, requesterId, reason, false);
    }

    /**
     * 강제 삭제 여부
     */
    public boolean isForceDelete() {
        return forceDelete != null && forceDelete;
    }

    /**
     * 삭제 이유가 있는지 확인
     */
    public boolean hasReason() {
        return deleteReason != null && !deleteReason.trim().isEmpty();
    }
}