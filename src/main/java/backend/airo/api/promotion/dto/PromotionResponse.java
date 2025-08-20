package backend.airo.api.promotion.dto;

import backend.airo.domain.promotion.Promotion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "홍보물 응답")
public record PromotionResponse(
        @Schema(description = "홍보물 ID", example = "1")
        Long id,

        @Schema(description = "게시물 ID", example = "123")
        Long postId,

        @Schema(description = "장소명", example = "맛있는 카페")
        String spotName,

        @Schema(description = "대표 이미지 URL", example = "https://example.com/image.jpg")
        String mainImageUrl,

        @Schema(description = "추천 태그", example = "[\"커피\", \"분위기\", \"맛집\"]")
        List<String> recommendedTags,

        @Schema(description = "감정 태그", example = "[\"행복\", \"만족\"]")
        List<String> emotions,

        @Schema(description = "제안된 제목", example = "감성 가득한 커피 한 잔")
        String suggestedTitle,

        @Schema(description = "홍보 내용", example = "직접 로스팅한 원두로 내린 커피가 일품입니다.")
        String content
) {
    public static PromotionResponse fromDomain(Promotion promotion) {
        return new PromotionResponse(
                promotion.getId(),
                promotion.getPostId(),
                promotion.getSpotName(),
                promotion.getMainImageUrl(),
                promotion.getRecommendedTags(),
                promotion.getEmotions(),
                promotion.getSuggestedTitle(),
                promotion.getContent()
        );
    }
}