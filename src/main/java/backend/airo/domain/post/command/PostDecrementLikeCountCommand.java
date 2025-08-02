package backend.airo.domain.post.command;

import backend.airo.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostDecrementLikeCountCommand {

    private final PostRepository postRepository;

    public int handle(Long postId) {
        return postRepository.decrementLikeCount(postId);
    }

}
