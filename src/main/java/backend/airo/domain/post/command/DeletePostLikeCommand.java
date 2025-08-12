package backend.airo.domain.post.command;

import backend.airo.domain.post.exception.InvalidPostLikeException;
import backend.airo.domain.post.exception.PostErrorCode;
import backend.airo.domain.post.exception.PostNotFoundException;
import backend.airo.domain.post.repository.PostLikeRepository;
import backend.airo.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeletePostLikeCommand {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    public int handle(Long postId, Long userId) {

        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException(postId, PostErrorCode.POST_NOT_FOUND);
        }

        return postLikeRepository.deleteByPostIdAndUserId(postId, userId);
    }

}
