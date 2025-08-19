package backend.airo.domain.promotion;

import backend.airo.domain.post.Post;
import backend.airo.domain.thumbnail.ThumbnailRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

public record PromotionResult(
        String spotName,
        String mainImageUrl,
        List<String> recommendedTags,
        List<String> emotions,
        String suggestedTitle,
        String content
) {

    public static PromotionResult from(Post post, List<String> imageUrls) {
        return new PromotionResult(
                post.getLocation() != null ? post.getLocation().toString() : "알 수 없는 장소",
                imageUrls.isEmpty() ? null : imageUrls.get(0),
                List.of(post.getCategory().name()),
                post.getEmotionTags().stream().map(Enum::name).toList(),
                post.getTitle(),
                post.getContent()
        );
    }
}