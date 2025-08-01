package backend.airo.persistence.post.entity;

import backend.airo.persistence.abstracts.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "post_likes")
public class PostLikeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id")
    private Long userId;

}
