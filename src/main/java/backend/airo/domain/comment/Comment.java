package backend.airo.domain.comment;

import lombok.Getter;

@Getter
public class Comment {

    private Long id;
    private final String content;
    private final Long postId;
    private final Long userId;

    public Comment(Long id, String content, Long postId, Long userId) {
        this.id = id;
        this.content = content;
        this.postId = postId;
        this.userId = userId;
    }
}
