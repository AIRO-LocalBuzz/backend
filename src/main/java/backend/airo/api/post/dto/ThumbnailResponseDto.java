package backend.airo.api.post.dto;

import backend.airo.domain.thumbnail.Thumbnail;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ThumbnailResponseDto {

    private Long id;
    private Long postId;
    private String spotName;
    private String mainImageUrl;
    private List<String> recommendedTags;
    private List<String> emotions;
    private String suggestedTitle;

    public static ThumbnailResponseDto fromDomain(Thumbnail thumbnail) {
        return new ThumbnailResponseDto(
                thumbnail.getId(),
                thumbnail.getPostId(),
                thumbnail.getSpotName(),
                thumbnail.getMainImageUrl(),
                thumbnail.getRecommendedTags(),
                thumbnail.getEmotions(),
                thumbnail.getSuggestedTitle()
        );
    }
}