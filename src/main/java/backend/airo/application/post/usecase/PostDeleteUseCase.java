package backend.airo.application.post.usecase;


import backend.airo.domain.post.Post;
import backend.airo.domain.post.command.DeletePostCommandService;
import backend.airo.domain.post.exception.PostAccessDeniedException;
import backend.airo.domain.post.query.GetPostQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static backend.airo.domain.post.exception.PostErrorCode.POST_ACCESS_DENIED;


/**
 * PostUseCase - 게시물 관련 비즈니스 로직 처리
 * Application Layer의 핵심 서비스
 */
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


        // 기존 게시물 조회
        Post existingPost = getPostQueryService.handle(postId);

        // 권한 검증
        validatePostOwnership(existingPost, requesterId);

        deletePostCommandService.handle(postId);

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