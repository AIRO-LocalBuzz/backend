package backend.airo.domain.promotion;

import backend.airo.domain.post.Post;

import java.util.List;

public record PromotionRequest(
        String content,
        String title,
        List<String> tags,
        String category,
        String location,
        List<String> imageUrls
) {
    public static PromotionRequest from(Post post, List<String> imageUrls) {
        return new PromotionRequest(
                post.getContent(),
                post.getTitle(),
                post.getEmotionTags().stream().map(Enum::name).toList(),
                post.getCategory().name(),
                post.getLocation() != null ? post.getLocation().toString() : null,
                imageUrls
        );
    }
}