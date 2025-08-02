package backend.airo.persistence.post.entity;


import backend.airo.domain.post.PostLike;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "post_like",
        uniqueConstraints =
        @UniqueConstraint(
                name = "ux_post_user",
                columnNames = {"post_id", "user_id"}
        )
)
public class PostLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    public PostLikeEntity(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public PostLikeEntity toEntity(PostLike postLike) {
        return new PostLikeEntity(postLike.getUserId(), postLike.getPostId());
    }

    public static PostLike toDomain(PostLikeEntity postLikeEntity) {
        return new PostLike(postLikeEntity.id, postLikeEntity.userId, postLikeEntity.postId);
    }
}
