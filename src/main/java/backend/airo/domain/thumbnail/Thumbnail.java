package backend.airo.domain.thumbnail;

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

    public static Thumbnail create(Long postId, ThumbnailResult result) {
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
}