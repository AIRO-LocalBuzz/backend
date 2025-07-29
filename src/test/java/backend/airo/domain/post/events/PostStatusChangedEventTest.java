package backend.airo.domain.post.events;

import backend.airo.domain.post.enums.PostStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostStatusChangedEventTest {

    @Test
    @DisplayName("게시물 상태 변경 이벤트 생성 테스트")
    void createPostStatusChangedEvent() {
        // given
        Long postId = 1L;
        Long userId = 1L;
        String title = "여행 후기";
        PostStatus previousStatus = PostStatus.DRAFT;
        PostStatus currentStatus = PostStatus.PUBLISHED;
        String changeReason = "사용자 요청에 의한 발행";

        // when
        PostStatusChangedEvent event = PostStatusChangedEvent.of(
                postId, userId, title, previousStatus, currentStatus, changeReason
        );

        // then
        assertThat(event.postId()).isEqualTo(postId);
        assertThat(event.userId()).isEqualTo(userId);
        assertThat(event.title()).isEqualTo(title);
        assertThat(event.previousStatus()).isEqualTo(previousStatus);
        assertThat(event.currentStatus()).isEqualTo(currentStatus);
        assertThat(event.changeReason()).isEqualTo(changeReason);
        assertThat(event.isValid()).isTrue();
        assertThat(event.isPublished()).isTrue();
        assertThat(event.isValidTransition()).isTrue();
    }

    @Test
    @DisplayName("상태 변경 팩토리 메서드 테스트")
    void statusChangeFactoryMethods() {
        // given
        Long postId = 1L;
        Long userId = 1L;
        String title = "여행 후기";
        PostStatus previousStatus = PostStatus.DRAFT;

        // when & then
        PostStatusChangedEvent toPublished = PostStatusChangedEvent.toPublished(postId, userId, title, previousStatus);
        assertThat(toPublished.currentStatus()).isEqualTo(PostStatus.PUBLISHED);
        assertThat(toPublished.isPublished()).isTrue();

        PostStatusChangedEvent toDraft = PostStatusChangedEvent.toDraft(postId, userId, title, PostStatus.PUBLISHED);
        assertThat(toDraft.currentStatus()).isEqualTo(PostStatus.DRAFT);
        assertThat(toDraft.isDraft()).isTrue();

        PostStatusChangedEvent toArchived = PostStatusChangedEvent.toArchived(postId, userId, title, PostStatus.PUBLISHED);
        assertThat(toArchived.currentStatus()).isEqualTo(PostStatus.ARCHIVED);
        assertThat(toArchived.isArchived()).isTrue();
    }
}