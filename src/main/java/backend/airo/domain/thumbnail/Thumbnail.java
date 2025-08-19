package backend.airo.domain.thumbnail;

import backend.airo.domain.promotion.PromotionResult;
import lombok.Getter;
import java.util.List;

@Getter
public class Thumbnail {
    private final Long id;
    private final Long postId;
    private final String spotName;
    private final String mainImageUrl;
    private final List<String> recommendedTags;
    private final List<String> emotions;
    private final String suggestedTitle;

    public Thumbnail(Long id, Long postId, String spotName, String mainImageUrl,
                     List<String> recommendedTags, List<String> emotions, String suggestedTitle) {
        this.id = id;
        this.postId = postId;
        this.spotName = spotName;
        this.mainImageUrl = mainImageUrl;
        this.recommendedTags = recommendedTags;
        this.emotions = emotions;
        this.suggestedTitle = suggestedTitle;
    }

    public static Thumbnail create(Long postId, PromotionResult result) {
        return new Thumbnail(
                null,
                postId,
                result.spotName(),
                result.mainImageUrl(),
                result.recommendedTags(),
                result.emotions(),
                result.suggestedTitle()
        );
    }

    public static Thumbnail createForTest(
            Long id,
            String mainImageUrl
    ) {
        Long postId = 1L; // Default postId for testing
        String spotName = "Test Spot";
        List<String> recommendedTags = List.of("test", "thumbnail");
        List<String> emotions = List.of("happy", "excited");
        String suggestedTitle = "Test Thumbnail Title";
        return new Thumbnail(id, postId, spotName, mainImageUrl, recommendedTags, emotions, suggestedTitle);
    }
}