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
import lombok.Setter;

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


    // PostEntity.java 수정
    public PostEntity(String title, String content, String summary, PostStatus status,
                      LocalDateTime travelDate, Integer viewCount, Integer likeCount,
                      Integer commentCount, Boolean isFeatured, LocalDateTime publishedAt) {
        super();
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.status = status;
        this.travelDate = travelDate;
        this.viewCount = viewCount != null ? viewCount : 0;
        this.likeCount = likeCount != null ? likeCount : 0;
        this.commentCount = commentCount != null ? commentCount : 0;
        this.isFeatured = isFeatured != null ? isFeatured : false;
        this.publishedAt = publishedAt;
    }

    public static PostEntity toEntity(Post post) {
        PostEntity entity = new PostEntity(
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

        // 연관관계는 별도 설정 (PostAdapter에서 처리)
        return entity;
    }

    public static Post toDomain(PostEntity entity) {
        return new Post(
                entity.getId(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getCategory() != null ? entity.getCategory().getId() : null,
                entity.getLocation() != null ? entity.getLocation().getId() : null,
                entity.getTitle(),
                entity.getContent(),
                entity.getSummary(),
                entity.getStatus(),
                entity.getTravelDate(),
                entity.getViewCount(),
                entity.getLikeCount(),
                entity.getCommentCount(),
                entity.getIsFeatured(),
                entity.getPublishedAt()
        );
    }

}