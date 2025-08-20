package backend.airo.domain.promotion;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Getter
public class Promotion {
    private final Long id;
    private final Long postId;
    private final String spotName;
    private final String mainImageUrl;
    private final List<String> recommendedTags;
    private final List<String> emotions;
    private final String suggestedTitle;
    private final String content;

    @JsonCreator
    public Promotion(
            @JsonProperty("id") Long id,
            @JsonProperty("postId") Long postId,
            @JsonProperty("spotName") String spotName,
            @JsonProperty("mainImageUrl") String mainImageUrl,
            @JsonProperty("recommendedTags") List<String> recommendedTags,
            @JsonProperty("emotions") List<String> emotions,
            @JsonProperty("suggestedTitle") String suggestedTitle,
            @JsonProperty("content") String content
    ) {
        this.id = id;
        this.postId = postId;
        this.spotName = spotName;
        this.mainImageUrl = mainImageUrl;
        this.recommendedTags = recommendedTags;
        this.emotions = emotions;
        this.suggestedTitle = suggestedTitle;
        this.content = content;
    }

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

    /**
     * PromotionResult로 변환
     */
    public PromotionResult getPromotionResult() {
        return new PromotionResult(
                this.spotName,
                this.mainImageUrl,
                this.recommendedTags,
                this.emotions,
                this.suggestedTitle,
                this.content
        );
    }
}