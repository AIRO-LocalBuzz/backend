package backend.airo.persistence.post.entity;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.*;
import backend.airo.persistence.abstracts.BaseEntity;
import backend.airo.domain.location.Location;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "posts")
public class PostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostWithWhoTag withWhoTag = PostWithWhoTag.ALLONE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostForWhatTag forWhatTag = PostForWhatTag.HEALING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostCategory category = PostCategory.RESTORANT;


    @ElementCollection
    @CollectionTable(name = "post_emotion_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "emotion_tag")
    private List<PostEmotionTag> emotionTags;

    @Column(name = "travel_date")
    private LocalDate travelDate;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0;

    @Column(name = "comment_count", nullable = false)
    private Integer commentCount = 0;

    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Embedded
    private Location location;

    @Column(name = "adress")
    private String adress;




    public PostEntity(Long userId, String title, String content, String summary,
                      PostStatus status, PostWithWhoTag withWhoTag, PostForWhatTag forWhatTag,
                      List<PostEmotionTag> emotionTags, PostCategory category, LocalDate travelDate, Location location, String adress, Boolean isFeatured, LocalDateTime publishedAt) {
        super();
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.status = status != null ? status : PostStatus.DRAFT;
        this.withWhoTag = withWhoTag != null ? withWhoTag : PostWithWhoTag.ALLONE;
        this.forWhatTag = forWhatTag != null ? forWhatTag : PostForWhatTag.HEALING;
        this.emotionTags = emotionTags;
        this.category = category != null ? category : PostCategory.RESTORANT;
        this.travelDate = travelDate;
        this.location = location;
        this.adress = adress;
        this.isFeatured = isFeatured != null ? isFeatured : false;
        this.viewCount = 0;
        this.likeCount = 0;
        this.commentCount = 0;
        this.isFeatured = false;
    }


    public static PostEntity toEntity(Post post) {
        return new PostEntity(
                post.getUserId(),
                post.getTitle(),
                post.getContent(),
                post.getSummary(),
                post.getStatus(),
                post.getWithWhoTag(),
                post.getForWhatTag(),
                post.getEmotionTags(),
                post.getCategory(),
                post.getTravelDate(),
                post.getLocation(),
                post.getAdress(),
                post.getIsFeatured(),
                post.getPublishedAt()
        );
    }


    public static Post toDomain(PostEntity entity) {
        return new Post(
                entity.getId(),
                entity.getUserId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getSummary(),
                entity.getStatus(),
                entity.getWithWhoTag(),
                entity.getForWhatTag(),
                entity.getEmotionTags(),
                entity.getCategory(),
                entity.getTravelDate(),
                entity.getLocation(),
                entity.getAdress(),
                entity.getViewCount(),
                entity.getLikeCount(),
                entity.getCommentCount(),
                entity.getIsFeatured(),
                entity.getPublishedAt()
        );
    }

}