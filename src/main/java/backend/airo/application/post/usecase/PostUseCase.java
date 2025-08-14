package backend.airo.application.post.usecase;

import backend.airo.api.post.dto.*;
import backend.airo.domain.image.Image;
import backend.airo.domain.image.query.GetImageQueryService;
import backend.airo.domain.point.command.UpsertPointCommand;
import backend.airo.domain.point_history.command.CreatePointHistoryCommand;
import backend.airo.domain.point_history.vo.PointType;
import backend.airo.domain.post.command.CreatePostCommandService;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.command.DeletePostCommandService;
import backend.airo.domain.post.command.UpdatePostCommandService;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.exception.PostException;
import backend.airo.domain.post.query.GetPostListQueryService;
import backend.airo.domain.post.query.GetPostQueryService;
import backend.airo.domain.post.vo.AuthorInfo;
import backend.airo.domain.thumbnail.Thumbnail;
import backend.airo.domain.user.User;
import backend.airo.domain.user.query.GetUserQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostUseCase {

    private final CreatePostCommandService createPostCommandService;
    private final UpsertPointCommand upsertPointCommand;
    private final CreatePointHistoryCommand createPointHistoryCommand;
    private final GetPostQueryService getPostQueryService;
    private final GetUserQuery getUserQueryService;
    private final GetImageQueryService getImageQueryService;
    private final GetPostListQueryService getPostListQueryService;
    private final UpdatePostCommandService updatePostCommandService;
    private final DeletePostCommandService deletePostCommandService;

    @Transactional
    public Post createPost(PostCreateRequest request, Long userId) {
        Post savedPost;

        if (request.status() != PostStatus.PUBLISHED ){
            savedPost = createPostCommandService.handle(request, userId);
        }else{
            savedPost = createPostCommandService.handle(request, userId);

            boolean handle = createPointHistoryCommand.handle(userId, 100L, savedPost.getId(), PointType.REPORT);
            if (handle) {
                upsertPointCommand.handle(userId, 100L);
            }
        }

        return savedPost;
    }


    public PostDetailResponse getPostDetail(Long postId, Long requesterId) {
        log.debug("게시물 조회: id={}, requesterId={}", postId, requesterId);
        Post post = getPostQueryService.handle(postId);

        if(!isPostOwner(post, requesterId)) {
            post.incrementViewCount();
        }

        AuthorInfo authorInfo = getAuthorInfo(post.getUserId());

        List<Image> imageList = new ArrayList<>(
                getImageQueryService.getImagesBelongsPost(postId)
        );

        return PostDetailResponse.toResponse(post, authorInfo, imageList);
    }


    public ThumbnailResponseDto getThumbnailById(Long thumbnailId) {
        Thumbnail Thumbnail = getPostQueryService.handleThumbnail(thumbnailId);
        return ThumbnailResponseDto.fromDomain(Thumbnail);
    }


    public Page<Post> getPostList(PostListRequest request) {
        log.debug("게시물 목록 조회: page={}, size={}", request.page(), request.size());
        return getPostListQueryService.handle(request);
    }

    public Slice<Post> getPostSlice(PostSliceRequest request) {
        log.debug("게시물 무한스크롤 조회: size={}, lastPostId={}",
                request.size(), request.lastPostId());
        return getPostListQueryService.handleSlice(request);
    }


    // private method

    private AuthorInfo getAuthorInfo(Long autherId) {
        User author = getUserQueryService.handle(autherId);
        return new AuthorInfo(author.getId(), author.getName(), author.getProfileImageUrl());
    }



    @Transactional
    public Post updatePost(Long postId, Long requesterId, PostUpdateRequest request) {
        log.info("게시물 수정 시작: id={}, requesterId={}", postId, requesterId);

        Post existingPost = getPostQueryService.handle(postId);

        validatePostOwnership(existingPost, requesterId);

        return updatePostCommandService.handle(request, existingPost);
    }


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