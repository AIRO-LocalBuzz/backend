package backend.airo.application.post.usecase;

import backend.airo.api.post.dto.PostDetailResponse;
import backend.airo.api.post.dto.PostListRequest;
import backend.airo.domain.image.Image;
import backend.airo.domain.image.query.GetImageQueryService;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.exception.PostAccessDeniedException;
import backend.airo.domain.post.query.GetPostListQueryService;
import backend.airo.domain.post.query.GetPostQueryService;
import backend.airo.domain.post.vo.AuthorInfo;
import backend.airo.domain.user.User;
import backend.airo.domain.user.query.GetUserQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static backend.airo.domain.post.exception.PostErrorCode.POST_ACCESS_DENIED;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostReadUseCase {

    private final GetPostQueryService getPostQueryService;
    private final GetUserQuery getUserQueryService;
    private final GetImageQueryService getImageQueryService;
    private final GetPostListQueryService getPostListQueryService;

    // ===== 게시물 조회 =====

    /**
     * 게시물 단건 조회
     */
    public PostDetailResponse getPostById(Long postId, Long requesterId) {
        log.debug("게시물 조회: id={}, requesterId={}", postId, requesterId);

        Post post = getPostQueryService.handle(postId);

        // 접근 권한 검증
        validatePostAccess(post, requesterId);

        // 조회수 증가
        if(!isPostOwner(post, requesterId)) {
            post.incrementViewCount();
        }

        AuthorInfo authorInfo = getAuthorInfo(post.getUserId());

        List<Image> imageList = new ArrayList<>(
                getImageQueryService.getImagesBelongsPost(postId)
        );

        return PostDetailResponse.fromDomain(post, authorInfo, imageList);
    }



    /**
     * 최근 게시물 목록 조회
     */
    public Page<Post> getRecentPostList(PostListRequest request) {
        log.debug("최근 게시물 목록 조회: page={}, size={}, userId={}",
                request.page(), request.size(), request.userId());

        return getPostListQueryService.getRecentPosts(request);

    }



    private AuthorInfo getAuthorInfo(Long autherId) {

        User author = getUserQueryService.handle(autherId);

        // 작성자 정보 반환
        return new AuthorInfo(author.getId(), author.getName(), author.getProfileImageUrl());
    }
    /**
     * 게시물 접근 권한 검증
     */
    private void validatePostAccess(Post post, Long requesterId) {
        if ( isPostOwner(post, requesterId)) {
            throw new PostAccessDeniedException(post.getId(), requesterId, POST_ACCESS_DENIED);
        }

        if (post.getStatus() != PostStatus.PUBLISHED || !isPostOwner(post, requesterId)) {
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