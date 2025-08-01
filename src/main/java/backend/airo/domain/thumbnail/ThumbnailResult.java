package backend.airo.domain.thumbnail;

import java.util.List;

public record ThumbnailResult(
        String mainImageUrl,
        List<String> recommendedTags,
        String suggestedTitle
) {}