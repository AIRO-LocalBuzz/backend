package backend.airo.persistence.comment.entity;

import backend.airo.domain.comment.Comment;
import backend.airo.persistence.abstracts.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private Long postId;

    private Long userId;

    public CommentEntity(String content, Long postId, Long userId) {
        this.content = content;
        this.postId = postId;
        this.userId = userId;
    }


    public static CommentEntity toEntity(Comment comment) {
        return new CommentEntity(
                comment.getContent(),
                comment.getPostId(),
                comment.getUserId()
        );
    }

    public static Comment toDomain(CommentEntity commentEntity) {
        return new Comment(
                commentEntity.id,
                commentEntity.content,
                commentEntity.postId,
                commentEntity.userId
        );
    }
}
