package backend.airo.domain.thumbnail;

import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ThumbnailRequestTest {

    @Test
    void shouldCreateThumbnailRequestFromPost() {
        // given
        Post post = createTestPost();
        List<String> imageUrls = List.of("url1", "url2");

        // when
        ThumbnailRequest request = ThumbnailRequest.from(post, imageUrls);

        // then
        assertThat(request.content()).isEqualTo(post.getContent());
        assertThat(request.imageUrls()).hasSize(2);
    }

    private Post createTestPost() {
        return Post.createForTest(
                1L,
                1L,
                "Test Title",
                "Test Content",
                List.of(PostEmotionTag.HAPPY, PostEmotionTag.EXCITED),
                PostCategory.CAFE,
                LocalDateTime.now()
        );
    }
}
