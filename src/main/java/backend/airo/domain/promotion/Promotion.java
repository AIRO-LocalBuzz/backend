package backend.airo.domain.promotion;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Promotion {
    private final Long id;
    private final Long postId;
    private final String spotName;
    private final String mainImageUrl;
    private final List<String> recommendedTags;
    private final List<String> emotions;
    private final String suggestedTitle;
    private final String content;

    public static Promotion create(Long postId, PromotionResult result) {
        return new Promotion(
                null,
                postId,
                result.spotName(),
                result.mainImageUrl(),
                result.recommendedTags(),
                result.emotions(),
                result.suggestedTitle(),
                result.content()
        );
    }
}