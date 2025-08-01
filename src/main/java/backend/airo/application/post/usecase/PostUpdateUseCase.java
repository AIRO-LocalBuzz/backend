package backend.airo.application.post.usecase;

import backend.airo.api.post.dto.PostUpdateRequest;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.command.UpdatePostCommandService;
import backend.airo.domain.post.exception.PostAccessDeniedException;
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
public class PostUpdateUseCase {

    private final GetPostQueryService getPostQueryService;
    private final UpdatePostCommandService updatePostCommandService;


    @Transactional
    public Post updatePost(Long postId, Long requesterId, PostUpdateRequest request) {
        log.info("게시물 수정 시작: id={}, requesterId={}", postId, requesterId);

        Post existingPost = getPostQueryService.handle(postId);

        validatePostOwnership(existingPost, requesterId);

        return updatePostCommandService.handle(request, existingPost);
    }


    private void validatePostOwnership(Post post, Long requesterId) {
        if (!isPostOwner(post, requesterId)) {
            throw new PostAccessDeniedException(post.getId(), requesterId, POST_ACCESS_DENIED);
        }
    }

    private boolean isPostOwner(Post post, Long userId) {
        return userId != null && userId.equals(post.getUserId());
    }


}