package backend.airo.api.post.dto;

import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;

import java.time.LocalDateTime;

public record PostSummaryResponse(
        Long id,
        String title,
        String content,
        PostStatus status,
        Integer viewCount,
        Long userId
//        LocalDateTime createdAt,
//        LocalDateTime updatedAt
) {
    public static PostSummaryResponse fromDomain(Post post) {
        return new PostSummaryResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getStatus(),
                post.getViewCount(),
                post.getUserId()
        );
    }
}