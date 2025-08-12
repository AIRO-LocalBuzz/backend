package backend.airo.domain.post.command;

import backend.airo.domain.post.exception.PostErrorCode;
import backend.airo.domain.post.exception.PostNotFoundException;
import backend.airo.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostDecrementLikeCountCommand {

    private final PostRepository postRepository;

    public int handle(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException(postId, PostErrorCode.POST_NOT_FOUND);
        }
        return postRepository.decrementLikeCount(postId);
    }

}
