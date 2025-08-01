package backend.airo.domain.post.events;

import backend.airo.domain.post.enums.PostStatus;
import java.time.LocalDateTime;

/**
 * 게시물 상태 변경 이벤트
 * 게시물의 상태가 변경될 때 발생하는 범용 이벤트
 */
public record PostStatusChangedEvent(
        Long postId,
        Long userId,
        String title,
        PostStatus previousStatus,
        PostStatus currentStatus,
        String changeReason,
        LocalDateTime occurredAt
) {

    public PostStatusChangedEvent(Long postId, Long userId, String title, PostStatus previousStatus, PostStatus currentStatus, String changeReason) {
        this(postId, userId, title, previousStatus, currentStatus, changeReason, LocalDateTime.now());
    }

    /**
     * 게시물 상태 변경 이벤트 생성
     */
    public static PostStatusChangedEvent of(Long postId, Long userId, String title, PostStatus previousStatus, PostStatus currentStatus, String changeReason) {
        return new PostStatusChangedEvent(
                postId,
                userId,
                title,
                previousStatus,
                currentStatus,
                changeReason
        );
    }

    /**
     * 임시저장으로 변경 이벤트
     */
    public static PostStatusChangedEvent toDraft(Long postId, Long userId, String title, PostStatus previousStatus) {
        return of(postId, userId, title, previousStatus, PostStatus.DRAFT, "사용자 요청에 의한 임시저장");
    }

    /**
     * 발행으로 변경 이벤트
     */
    public static PostStatusChangedEvent toPublished(Long postId, Long userId, String title, PostStatus previousStatus) {
        return of(postId, userId, title, previousStatus, PostStatus.PUBLISHED, "사용자 요청에 의한 발행");
    }

    /**
     * 보관으로 변경 이벤트
     */
    public static PostStatusChangedEvent toArchived(Long postId, Long userId, String title, PostStatus previousStatus) {
        return of(postId, userId, title, previousStatus, PostStatus.ARCHIVED, "사용자 요청에 의한 보관");
    }

    /**
     * 이벤트 유효성 검증
     */
    public boolean isValid() {
        return postId != null
                && userId != null
                && title != null
                && !title.trim().isEmpty()
                && previousStatus != null
                && currentStatus != null
                && previousStatus != currentStatus
                && changeReason != null
                && !changeReason.trim().isEmpty()
                && occurredAt != null;
    }

    /**
     * 발행 상태로 변경되었는지 확인
     */
    public boolean isPublished() {
        return currentStatus == PostStatus.PUBLISHED;
    }

    /**
     * 보관 상태로 변경되었는지 확인
     */
    public boolean isArchived() {
        return currentStatus == PostStatus.ARCHIVED;
    }

    /**
     * 임시저장 상태로 변경되었는지 확인
     */
    public boolean isDraft() {
        return currentStatus == PostStatus.DRAFT;
    }

    /**
     * 상태 변경이 유효한 전환인지 검증
     */
    public boolean isValidTransition() {
        return switch (previousStatus) {
            case DRAFT -> currentStatus == PostStatus.PUBLISHED;
            case PUBLISHED -> currentStatus == PostStatus.ARCHIVED || currentStatus == PostStatus.DRAFT;
            case ARCHIVED -> currentStatus == PostStatus.PUBLISHED || currentStatus == PostStatus.DRAFT;
        };
    }
}