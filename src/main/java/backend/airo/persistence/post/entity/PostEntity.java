package backend.airo.persistence.post.entity;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.persistence.abstracts.BaseEntity;
import backend.airo.persistence.category.entity.CategoryEntity;
import backend.airo.persistence.comment.entity.CommentEntity;
import backend.airo.persistence.image.entity.ImageEntity;
import backend.airo.persistence.location.entity.LocationEntity;
import backend.airo.persistence.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "posts")
public class PostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.DRAFT;

    @Column(name = "travel_date")
    private LocalDateTime travelDate;

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

    // 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sortOrder ASC")
    private List<ImageEntity> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostTagEntity> postTags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostLikeEntity> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("createdAt ASC")
    private List<CommentEntity> comments = new ArrayList<>();


    public PostEntity(Long userId ,String title, String content, String summary, PostStatus status, LocalDateTime travelDate, Integer viewCount, Integer likeCount, Integer commentCount, Boolean isFeatured, LocalDateTime publishedAt) {
        super();
    }

    public static PostEntity toEntity(Post post) {
        return new PostEntity(
                post.getUserId(),
                post.getTitle(),
                post.getContent(),
                post.getSummary(),
                post.getStatus(),
                post.getTravelDate(),
                post.getViewCount(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getIsFeatured(),
                post.getPublishedAt()
        );
    }

    public static Post toDomain(PostEntity post) {
        return new Post(
                post.getId(),
                post.getUser().getId(),
                post.getCategory() != null ? post.getCategory().getId() : null,
                post.getLocation() != null ? post.getLocation().getId() : null,
                post.getTitle(),
                post.getContent(),
                post.getSummary(),
                post.getStatus(),
                post.getTravelDate(),
                post.getViewCount(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getIsFeatured(),
                post.getPublishedAt()
        );
    }

}