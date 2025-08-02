package backend.airo.domain.post.event;

import backend.airo.domain.post.enums.PostStatus;
import java.time.LocalDateTime;

/**
 * 게시물 발행 이벤트
 * 게시물이 DRAFT에서 PUBLISHED 상태로 변경될 때 발생
 */
public record PostPublishedEvent(
        Long postId,
        Long userId,
        String title,
        PostStatus previousStatus,
        PostStatus currentStatus,
        LocalDateTime publishedAt,
        LocalDateTime occurredAt
) {

    public PostPublishedEvent(Long postId, Long userId, String title, PostStatus previousStatus, PostStatus currentStatus, LocalDateTime publishedAt) {
        this(postId, userId, title, previousStatus, currentStatus, publishedAt, LocalDateTime.now());
    }

    /**
     * 게시물 발행 이벤트 생성
     */
    public static PostPublishedEvent of(Long postId, Long userId, String title, PostStatus previousStatus, LocalDateTime publishedAt) {
        return new PostPublishedEvent(
                postId,
                userId,
                title,
                previousStatus,
                PostStatus.PUBLISHED,
                publishedAt
        );
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
                && currentStatus == PostStatus.PUBLISHED
                && publishedAt != null
                && occurredAt != null;
    }

    /**
     * 알림 발송 대상 여부 확인
     */
    public boolean shouldNotifyFollowers() {
        return currentStatus == PostStatus.PUBLISHED;
    }
}