package backend.airo.domain.thumbnail;

import backend.airo.domain.post.Post;
import java.util.List;

public record ThumbnailRequest(
        String content,
        String title,
        List<String> tags,
        String category,
        String location,
        List<String> imageUrls
) {
    public static ThumbnailRequest from(Post post, List<String> imageUrls) {
        return new ThumbnailRequest(
                post.getContent(),
                post.getTitle(),
                post.getEmotionTags().stream().map(Enum::name).toList(),
                post.getCategory().name(),
                post.getLocation() != null ? post.getLocation().toString() : null,
                imageUrls
        );
    }
}