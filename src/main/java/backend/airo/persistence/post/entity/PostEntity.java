package backend.airo.persistence.post.entity;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.*;
import backend.airo.persistence.abstracts.BaseEntity;
import backend.airo.domain.post.vo.Location;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Column(columnDefinition = "TEXT")
    private String businessName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.DRAFT;


    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private PostForWhatTag forWhatTag = PostForWhatTag.HEALING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostCategory category = PostCategory.RESTAURANT;


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

    @Column(name = "address")
    private String address;




    public PostEntity(Long postId, Long userId, String title, String content, String summary, String businessName,
                      PostStatus status, PostForWhatTag forWhatTag,
                      List<PostEmotionTag> emotionTags, PostCategory category, LocalDate travelDate, Location location, String address, Boolean isFeatured, LocalDateTime publishedAt) {
        super();
        this.id = postId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.businessName = businessName;
        this.status = status != null ? status : PostStatus.DRAFT;
        this.forWhatTag = forWhatTag != null ? forWhatTag : PostForWhatTag.HEALING;
        this.emotionTags = emotionTags;
        this.category = category != null ? category : PostCategory.RESTAURANT;
        this.travelDate = travelDate;
        this.location = location;
        this.address = address;
        this.isFeatured = isFeatured != null ? isFeatured : false;
        this.viewCount = 0;
        this.likeCount = 0;
        this.commentCount = 0;
        this.publishedAt = publishedAt != null ? publishedAt : LocalDateTime.now();
    }


    public static PostEntity toEntity(Post post) {
        return new PostEntity(
                post.getId(),
                post.getUserId(),
                post.getTitle(),
                post.getContent(),
                post.getSummary(),
                post.getBusinessName(),
                post.getStatus(),
                post.getForWhatTag(),
                post.getEmotionTags(),
                post.getCategory(),
                post.getTravelDate(),
                post.getLocation(),
                post.getAddress(),
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
                entity.getBusinessName(),
                entity.getStatus(),
                entity.getForWhatTag(),
                entity.getEmotionTags(),
                entity.getCategory(),
                entity.getTravelDate(),
                entity.getLocation(),
                entity.getAddress(),
                entity.getViewCount(),
                entity.getLikeCount(),
                entity.getCommentCount(),
                entity.getIsFeatured(),
                entity.getPublishedAt()
        );
    }

}