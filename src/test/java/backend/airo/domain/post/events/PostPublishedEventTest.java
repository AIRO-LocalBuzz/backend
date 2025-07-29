// 테스트 코드
package backend.airo.domain.post.events;

import backend.airo.domain.post.enums.PostStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PostPublishedEventTest {

    @Test
    @DisplayName("게시물 발행 이벤트 생성 테스트")
    void createPostPublishedEvent() {
        // given
        Long postId = 1L;
        Long userId = 1L;
        String title = "여행 후기";
        PostStatus previousStatus = PostStatus.DRAFT;
        LocalDateTime publishedAt = LocalDateTime.now();

        // when
        PostPublishedEvent event = PostPublishedEvent.of(postId, userId, title, previousStatus, publishedAt);

        // then
        assertThat(event.postId()).isEqualTo(postId);
        assertThat(event.userId()).isEqualTo(userId);
        assertThat(event.title()).isEqualTo(title);
        assertThat(event.previousStatus()).isEqualTo(previousStatus);
        assertThat(event.currentStatus()).isEqualTo(PostStatus.PUBLISHED);
        assertThat(event.publishedAt()).isEqualTo(publishedAt);
        assertThat(event.isValid()).isTrue();
        assertThat(event.shouldNotifyFollowers()).isTrue();
    }
}

