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

/**
 * PostUseCase - 게시물 관련 비즈니스 로직 처리
 * Application Layer의 핵심 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostUpdateUseCase {

    private final GetPostQueryService getPostQueryService;
    private final UpdatePostCommandService updatePostCommandService;

    /**
     * 게시물 수정
     */
    @Transactional
    public Post updatePost(Long postId, Long requesterId, PostUpdateRequest request) {
        log.info("게시물 수정 시작: id={}, requesterId={}", postId, requesterId);

        // 기존 게시물 조회
        Post existingPost = getPostQueryService.handle(postId);

        // 권한 검증
        validatePostOwnership(existingPost, requesterId);

        return updatePostCommandService.handle(request, existingPost);
    }


    /**
     * 게시물 소유권 검증
     */
    private void validatePostOwnership(Post post, Long requesterId) {
        if (!isPostOwner(post, requesterId)) {
            throw new PostAccessDeniedException(post.getId(), requesterId, POST_ACCESS_DENIED);
        }
    }

    /**
     * 게시물 소유자 확인
     */
    private boolean isPostOwner(Post post, Long userId) {
        return userId != null && userId.equals(post.getUserId());
    }


}