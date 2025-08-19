package backend.airo.domain.thumbnail;

import java.util.List;

public record ThumbnailResult(
        String spotName,
        String mainImageUrl,
        List<String> recommendedTags,
        List<String> emotions,
        String suggestedTitle
) {
}