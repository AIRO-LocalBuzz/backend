package backend.airo.api.post.dto;

import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostEmotionTag;
import backend.airo.domain.post.enums.PostStatus;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public record PostSummaryResponse(
        Long id,
        String title,
        String content,
        PostStatus status,
        Integer viewCount,
        List<PostEmotionTag> emotionTags,
        Long userId
//        LocalDateTime createdAt,
//        LocalDateTime updatedAt
) {
    public static PostSummaryResponse fromDomain(Post post) {
        return new PostSummaryResponse(
                post.id(),
                post.title(),
                post.content(),
                post.status(),
                post.viewCount(),
                post.emotionTags() != null ?
                        new ArrayList<>(post.emotionTags()) : new ArrayList<>(),
                post.userId()
        );
    }

    public PostSummaryResponse(
            Long id,
            String title,
            String content,
            PostStatus status,
            Integer viewCount,
            Long userId
    ) {
        this(id, title, content, status, viewCount, new ArrayList<>(), userId);
    }
}