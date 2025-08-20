package backend.airo.application.post.usecase;

import backend.airo.api.post.dto.*;
import backend.airo.cache.post.PostCacheService;
import backend.airo.cache.post.dto.PostSliceCacheDto;
import backend.airo.domain.image.Image;
import backend.airo.domain.image.query.GetImageQueryService;
import backend.airo.domain.point.command.UpsertPointCommand;
import backend.airo.domain.point_history.command.CreatePointHistoryCommand;
import backend.airo.domain.point_history.vo.PointType;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.command.CreatePostCommandService;
import backend.airo.domain.post.command.DeletePostCommandService;
import backend.airo.domain.post.command.UpdatePostCommandService;
import backend.airo.domain.post.command.UpdatePostViewCountCommand;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.exception.PostException;
import backend.airo.domain.post.query.GetPostQueryService;
import backend.airo.domain.post.vo.AuthorInfo;
import backend.airo.domain.thumbnail.Thumbnail;
import backend.airo.domain.user.User;
import backend.airo.domain.user.query.GetUserQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostCacheUseCase {

    private final CreatePostCommandService createPostCommandService;
    private final UpdatePostCommandService updatePostCommandService;
    private final DeletePostCommandService deletePostCommandService;
    private final UpsertPointCommand upsertPointCommand;
    private final CreatePointHistoryCommand createPointHistoryCommand;
    private final UpdatePostViewCountCommand updatePostViewCountCommand;

    private final GetPostQueryService getPostQueryService;
    private final GetUserQuery getUserQueryService;
    private final GetImageQueryService getImageQueryService;

    private final PostCacheService postCacheService;



    public Post createPost(PostCreateRequest request, Long userId) {
        Post savedPost;

        if (request.status() != PostStatus.PUBLISHED ){
            savedPost = createPostCommandService.handle(request, userId);
        }else{
            savedPost = createPostCommandService.handle(request, userId);

            boolean handle = createPointHistoryCommand.handle(userId, 100L, savedPost.id(), PointType.REPORT);
            if (handle) {
                upsertPointCommand.handle(userId, 100L);
            }
        }

        postCacheService.evictPostListCaches();

        return savedPost;
    }



    public PostDetailResponse getPostDetail(Long postId, Long requesterId) {
        log.info("게시물 조회: id={}, requesterId={}", postId, requesterId);

        Post post = postCacheService.getPost(postId);
        if(!post.isPostOwner(requesterId)) {
            updatePostViewCountCommand.handle(postId);
            postCacheService.evictPostCaches(postId);
        }

        AuthorInfo authorInfo = getAuthorInfo(post.userId());
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
        return postCacheService.getPostList(request);
    }



    public Slice<PostSummaryResponse> getPostSlice(PostSliceRequest request) {
        log.debug("게시물 무한스크롤 조회: size={}, lastPostId={}",
                request.size(), request.lastPostId());


        PostSliceCacheDto cachedSlice = getLatestCachedPostSummary(request);

        return cachedSlice.toSlice(Pageable.ofSize(request.size()));
    }



    public Post updatePost(Long postId, Long requesterId, PostUpdateRequest request) {
        log.info("게시물 수정 시작: id={}, requesterId={}", postId, requesterId);

        Post existingPost = getPostQueryService.handle(postId);

        validatePostOwnership(existingPost, requesterId);

        Post updatedPost = updatePostCommandService.handle(request, existingPost);

        // 수정 시 관련 캐시 무효화
        postCacheService.evictPostCaches(postId);
        postCacheService.evictPostListCaches();

        return updatedPost;
    }


    public void deletePost(Long postId, Long requesterId) {
        log.info("게시물 삭제 시작: id={}, requesterId={}", postId, requesterId);

        Post existingPost = getPostQueryService.handle(postId);

        validatePostOwnership(existingPost, requesterId);

        deletePostCommandService.handle(postId);

        postCacheService.evictPostCaches(postId);
        postCacheService.evictPostListCaches();

    }



    // private method

    private AuthorInfo getAuthorInfo(Long autherId) {
        User author = getUserQueryService.handle(autherId);
        return new AuthorInfo(author.getId(), author.getName(), author.getProfileImageUrl());
    }

    private void validatePostOwnership(Post post, Long requesterId) {
        if (!isPostOwner(post, requesterId)) {
            throw PostException.accessDenied(post.id(), requesterId);
        }
    }

    private boolean isPostOwner(Post post, Long userId) {
        return userId != null && userId.equals(post.userId());
    }


    private PostSliceCacheDto getLatestCachedPostSummary(PostSliceRequest request) {
        log.info("최신 캐시된 게시물 요약 조회: {}", request);
        Long maxPostId = getPostQueryService.getMaxPostId();
        long startId = request.lastPostId()> maxPostId? maxPostId : request.lastPostId()-1;
        log.info("최대 게시물 ID: {}, 시작 ID: {}", maxPostId, startId);
            List<PostSummaryResponse> cachedSummaries = new ArrayList<>();

            for (long id = startId; id > startId- request.size() && id > 0 ; id--) {
                PostSummaryResponse cached = postCacheService.getPostSummary(id);
                if (!isNullSummary(cached)) {
                    cachedSummaries.add(cached);
                }
            }


        boolean hasNext = false;
        if (!cachedSummaries.isEmpty()) {
            Long lastReturnedId = cachedSummaries.get(cachedSummaries.size() - 1).id();
            // DB에서 lastReturnedId보다 작은 ID가 존재하는지 확인
            hasNext = getPostQueryService.existsPostWithIdLessThan(lastReturnedId);
        }
            log.info("캐시+DB 조회 완료: {}개, hasNext={}", cachedSummaries.size(), hasNext);

            return new PostSliceCacheDto(
                    cachedSummaries,
                    hasNext,
                    request.size(),
                    cachedSummaries.size()
            );

    }


    private boolean isNullSummary(PostSummaryResponse summary) {
        return summary.id() == null || summary.title() == null;
    }
}