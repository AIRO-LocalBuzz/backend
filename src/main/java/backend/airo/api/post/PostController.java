package backend.airo.api.post;

import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.global.dto.Response;
import backend.airo.api.global.swagger.PostControllerSwagger;
import backend.airo.application.post.usecase.PostCacheUseCase;
import backend.airo.domain.post.Post;
import backend.airo.api.post.dto.*;
import backend.airo.domain.user.User;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Validated
@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController implements PostControllerSwagger {

    private final PostCacheUseCase postUseCase;

    // ===== 게시물 생성 =====

    @Override
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Response<PostResponse> createPost(
            @Valid @RequestBody PostCreateRequest request,
            @UserPrincipal User user) {
        log.info("게시물 생성 요청 원본: {}", request);

        log.info("게시물 생성 요청: title={}, userId={}", request.title(), user.getId());

        Post createdPost = postUseCase.createPost(request, user.getId());
        PostResponse response = PostResponse.fromDomain(createdPost);

        return Response.success(response);
    }


    // ===== 게시물 조회 =====
    @Override
    @GetMapping("/{postId}")
    public Response<PostDetailResponse> getPost(
            @Parameter(description = "게시물 ID", required = true)
            @UserPrincipal User user,
            @PathVariable @Positive Long postId) {

        PostDetailResponse response = postUseCase.getPostDetail(postId, user.getId());

        return Response.success(response);
    }

    @Override
    @GetMapping("thumbnail/{thumbnailId}")
    public Response<ThumbnailResponseDto> getThumbnail(
            @Parameter(description = "썸네일 ID", required = true)
            @UserPrincipal User user,
            @PathVariable @Positive Long thumbnailId) {

        ThumbnailResponseDto response = postUseCase.getThumbnailById(thumbnailId);

        return Response.success(response);
    }

    // ===== 게시물 List조회 =====

    @Override
    @GetMapping
    public Response<PostListResponse> getPostList(
            @Valid @ModelAttribute PostListRequest request) {

        Page<Post> postPage = postUseCase.getPostList(request);
        PostListResponse response = PostListResponse.fromDomain(postPage);

        return Response.success(response);
    }

    @Override
    @GetMapping("/scroll")
    public Response<PostSliceResponse> getPostSlice(
            @Valid @ModelAttribute PostSliceRequest request) {

        log.debug("무한스크롤 조회 요청: size={}, lastPostId={}",
                request.size(), request.lastPostId());

        Slice<PostSummaryResponse> postSlice = postUseCase.getPostSlice(request);
        PostSliceResponse response = PostSliceResponse.fromDomain(postSlice);

        return Response.success(response);
    }


    @Override
    @GetMapping("/my")
    public Response<PostListResponse> getMyPost(
            @Valid @ModelAttribute PostListRequest request,
            @UserPrincipal User user) {

        PostListResponse response = postUseCase.getMyPostList(request, user.getId());

        return Response.success(response);
    }



    // ===== 게시물 수정 =====
    @Override
    @PutMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    public Response<PostResponse> updatePost(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable @Positive Long postId,
            @Valid @RequestBody PostUpdateRequest request,
            @UserPrincipal User user) {
        Post updatedPost = postUseCase.updatePost(postId, user.getId(), request);

        PostResponse response = PostResponse.fromDomain(updatedPost);

        return Response.success(response);
    }

    // ===== 게시물 삭제 =====
    @Override
    @DeleteMapping("/{postId}")
    public Response<Void> deletePost(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable @Positive Long postId,
            @UserPrincipal User user) {

        postUseCase.deletePost(postId, user.getId());

        return Response.success("삭제 성공");
    }




//    @Override
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/thumbnail")
//    public Response<PostResponse> createPostAndThumbnail(
//            @Valid @RequestBody PostCreateRequest request,
//            @UserPrincipal User user) {
//
//        log.info("게시물+썸네일 생성 요청: title={}, userId={}", request.title(), user.getId());
//
//        Post createdPost = postCreateUseCase.createPostAndThumbnail(request, user.getId());
//        PostResponse response = PostResponse.fromDomain(createdPost);
//
//        return Response.success(response);
//    }



//    // ===== 통계 API =====
//
//    @GetMapping("/popular")
//    public ResponseEntity<List<PostResponse>> getPopularPosts(
//            @Valid @ModelAttribute PopularPostsRequest request) {
//
//        List<Post> popularPosts = postUseCase.getPopularPosts(request.limit());
//        List<PostResponse> responses = popularPosts.stream()
//                .map(PostResponse::fromDomain)
//                .toList();
//
//        return ResponseEntity.ok(responses);
//    }
//
//
//
//    @GetMapping("/recent-popular")
//    public ResponseEntity<List<PostResponse>> getRecentPopularPosts(
//            @Valid @ModelAttribute RecentPopularPostsRequest request) {
//
//        List<Post> recentPopularPosts = postUseCase.getRecentPopularPosts(request.days(), request.limit());
//        List<PostResponse> responses = recentPopularPosts.stream()
//                .map(PostResponse::fromDomain)
//                .toList();
//
//        return ResponseEntity.ok(responses);
//    }

    // ===== Private Helper Methods =====
}