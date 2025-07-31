package backend.airo.persistence.post.entity;

import backend.airo.persistence.post.entity.key.PostTagId;
import backend.airo.persistence.tag.entity.TagEntity;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_tags")
public class PostTagEntity {

    @EmbeddedId
    @Column(name = "id")
    private PostTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private TagEntity tag;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


}
