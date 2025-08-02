package backend.airo.domain.post.command;

import backend.airo.domain.post.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeletePostLikeCommand {

    private final PostLikeRepository postLikeRepository;

    public int handle(Long postId, Long userId) {
        return postLikeRepository.deleteByPostIdAndUserId(postId, userId);
    }

}
