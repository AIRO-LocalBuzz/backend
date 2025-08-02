package backend.airo.persistence.thumbnail.entity;

import backend.airo.domain.thumbnail.Thumbnail;
import backend.airo.persistence.abstracts.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "thumbnails")
public class ThumbnailEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id", nullable = false, unique = true)
    private Long postId;

    @Column(name = "spot_name")
    private String spotName;

    @Column(name = "main_image_url", columnDefinition = "TEXT")
    private String mainImageUrl;

    @ElementCollection
    @CollectionTable(name = "thumbnail_recommended_tags", joinColumns = @JoinColumn(name = "thumbnail_id"))
    @Column(name = "tag")
    private List<String> recommendedTags;

    @ElementCollection
    @CollectionTable(name = "thumbnail_emotions", joinColumns = @JoinColumn(name = "thumbnail_id"))
    @Column(name = "emotion")
    private List<String> emotions;

    @Column(name = "suggested_title")
    private String suggestedTitle;

    public ThumbnailEntity(Long postId, String spotName, String mainImageUrl,
                           List<String> recommendedTags, List<String> emotions, String suggestedTitle) {
        this.postId = postId;
        this.spotName = spotName;
        this.mainImageUrl = mainImageUrl;
        this.recommendedTags = recommendedTags;
        this.emotions = emotions;
        this.suggestedTitle = suggestedTitle;
    }

    public static ThumbnailEntity toEntity(Thumbnail thumbnail) {
        return new ThumbnailEntity(
                thumbnail.getPostId(),
                thumbnail.getSpotName(),
                thumbnail.getMainImageUrl(),
                thumbnail.getRecommendedTags(),
                thumbnail.getEmotions(),
                thumbnail.getSuggestedTitle()
        );
    }

    public static Thumbnail toDomain(ThumbnailEntity entity) {
        return new Thumbnail(
                entity.getId(),
                entity.getPostId(),
                entity.getSpotName(),
                entity.getMainImageUrl(),
                entity.getRecommendedTags(),
                entity.getEmotions(),
                entity.getSuggestedTitle()
        );
    }
}