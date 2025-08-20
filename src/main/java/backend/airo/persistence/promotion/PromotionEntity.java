package backend.airo.persistence.promotion;

import backend.airo.domain.promotion.Promotion;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "promotions")
public class PromotionEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "post_id", nullable = false, unique = true)
        private Long postId;

        @Column(name = "spot_name")
        private String spotName;

        @Column(name = "main_image_url", columnDefinition = "TEXT")
        private String mainImageUrl;

        @ElementCollection(fetch = FetchType.EAGER)
        @CollectionTable(name = "promotion_recommended_tags", joinColumns = @JoinColumn(name = "promotion_id"))
        @Column(name = "tag")
        private List<String> recommendedTags;


        @ElementCollection(fetch = FetchType.EAGER)
        @CollectionTable(name = "promotion_emotions", joinColumns = @JoinColumn(name = "promotion_id"))
        @Column(name = "emotion")
        private List<String> emotions;

        @Column(name = "suggested_title")
        private String suggestedTitle;

        @Column(name = "suggested_content")
        private String content;



    public PromotionEntity(Long postId, String spotName, String mainImageUrl,
                           List<String> recommendedTags, List<String> emotions,
                           String suggestedTitle, String content) {
        this.postId = postId;
        this.spotName = spotName;
        this.mainImageUrl = mainImageUrl;
        this.recommendedTags = recommendedTags;
        this.emotions = emotions;
        this.suggestedTitle = suggestedTitle;
        this.content = content;
    }

    public static PromotionEntity toEntity(Promotion promotion) {
        return new PromotionEntity(
                promotion.getPostId(),
                promotion.getSpotName(),
                promotion.getMainImageUrl(),
                promotion.getRecommendedTags(),
                promotion.getEmotions(),
                promotion.getSuggestedTitle(),
                promotion.getContent()
        );
    }

    public static Promotion toDomain(PromotionEntity entity) {
        return new Promotion(
                entity.getId(),
                entity.getPostId(),
                entity.getSpotName(),
                entity.getMainImageUrl(),
                entity.getRecommendedTags(),
                entity.getEmotions(),
                entity.getSuggestedTitle(),
                entity.getContent()
        );
    }
}
