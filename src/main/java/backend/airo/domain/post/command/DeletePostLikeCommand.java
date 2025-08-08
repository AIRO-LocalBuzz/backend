package backend.airo.domain.post.command;

import backend.airo.domain.post.exception.InvalidPostLikeException;
import backend.airo.domain.post.exception.PostErrorCode;
import backend.airo.domain.post.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeletePostLikeCommand {

    private final PostLikeRepository postLikeRepository;

    public int handle(Long postId, Long userId) {
        if (postId == null) {
            throw new InvalidPostLikeException(PostErrorCode.POST_ID_REQUIRED, "postId", "null");
        }
        if (userId == null) {
            throw new InvalidPostLikeException(PostErrorCode.USER_ID_REQUIRED, "userId", "null");
        }
        if (postId <= 0) {
            throw new InvalidPostLikeException(PostErrorCode.POST_ID_POSITIVE, "postId", postId.toString());
        }
        if (userId <= 0) {
            throw new InvalidPostLikeException(PostErrorCode.USER_ID_POSITIVE, "userId", userId.toString());
        }

        return postLikeRepository.deleteByPostIdAndUserId(postId, userId);
    }

}
