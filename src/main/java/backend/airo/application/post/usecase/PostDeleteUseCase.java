package backend.airo.application.post.usecase;


import backend.airo.domain.post.Post;
import backend.airo.domain.post.command.DeletePostCommandService;
import backend.airo.domain.post.exception.PostException;
import backend.airo.domain.post.query.GetPostQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static backend.airo.domain.post.exception.PostErrorCode.POST_ACCESS_DENIED;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostDeleteUseCase {

    private final GetPostQueryService getPostQueryService;
    private final DeletePostCommandService deletePostCommandService;

    @Transactional
    public void deletePost(Long postId, Long requesterId) {
        log.info("게시물 삭제 시작: id={}, requesterId={}", postId, requesterId);

        Post existingPost = getPostQueryService.handle(postId);

        validatePostOwnership(existingPost, requesterId);

        deletePostCommandService.handle(postId);

    }



    private void validatePostOwnership(Post post, Long requesterId) {
        if (!isPostOwner(post, requesterId)) {
            throw PostException.accessDenied(post.getId(), requesterId);
        }
    }

    private boolean isPostOwner(Post post, Long userId) {
        return userId != null && userId.equals(post.getUserId());
    }

}