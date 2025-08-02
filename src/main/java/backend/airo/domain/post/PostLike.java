package backend.airo.domain.post;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostLike {

    private Long id;
    private final Long userId;
    private final Long postId;

    public PostLike(Long id, Long userId, Long postId) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
    }
}
