package backend.airo.api.post;

import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.global.swagger.PostControllerSwagger;
import backend.airo.application.post.usecase.PostCreateUseCase;
import backend.airo.application.post.usecase.PostDeleteUseCase;
import backend.airo.application.post.usecase.PostReadUseCase;
import backend.airo.application.post.usecase.PostUpdateUseCase;
import backend.airo.domain.post.Post;
import backend.airo.api.post.dto.*;
import backend.airo.domain.user.User;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController implements PostControllerSwagger {

    private final PostCreateUseCase postCreateUseCase;
    private final PostReadUseCase postReadUseCase;
    private final PostUpdateUseCase postUpdateUseCase;
    private final PostDeleteUseCase postDeleteUseCase;

    // ===== 게시물 생성 =====

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody PostCreateRequest request,
            @UserPrincipal User user) {
        log.info("게시물 생성 요청 원본: {}", request);
        log.info("세부 정보 - title: {}, userId: {}, categoryId: {}, locationId: {}",
                request.title(), request.userId(), request.category(), request.location());

        log.info("게시물 생성 요청: title={}, userId={}", request.title(), request.userId());


        Post createdPost = postCreateUseCase.createPost(request);
        PostResponse response = PostResponse.fromDomain(createdPost);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ===== 게시물 조회 =====


    @GetMapping("/{id}")
    public ResponseEntity<PostDetailResponse> getPost(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable Long userId,
            @PathVariable Long postId,
            HttpServletRequest httpRequest) {

        Long requesterId = getCurrentUserId(httpRequest);

        PostDetailResponse response = postReadUseCase.getPostById(postId, userId);

        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<PostListResponse> getPostList(
            @Valid @ModelAttribute PostListRequest request,
            HttpServletRequest httpRequest) {

        Page<Post> postPage = postReadUseCase.getRecentPostList(request);
        PostListResponse response = PostListResponse.fromDomain(postPage);

        return ResponseEntity.ok(response);
    }


    // ===== 게시물 수정 =====

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest request,
            HttpServletRequest httpRequest) {

        Long requesterId = getCurrentUserId(httpRequest);

        Post updatedPost = postUpdateUseCase.updatePost(id, requesterId, request);
        PostResponse response = PostResponse.fromDomain(updatedPost);

        return ResponseEntity.ok(response);
    }



    // ===== 게시물 삭제 =====

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable Long id,
            HttpServletRequest httpRequest) {

        Long requesterId = getCurrentUserId(httpRequest);

        postDeleteUseCase.deletePost(id, requesterId);

        return ResponseEntity.noContent().build();
    }


//
//    // ===== 게시물 좋아요 =====
//
//    @PostMapping("/{id}/like")
//    public ResponseEntity<LikeResponse> likePost(
//            @Parameter(description = "게시물 ID", required = true)
//            @PathVariable Long id,
//            HttpServletRequest request) {
//
//        Long userId = getCurrentUserId(request);
//        boolean isLiked = postUseCase.likePost(id, userId);
//
//        LikeResponse response = new LikeResponse(isLiked, isLiked ? "좋아요 추가" : "좋아요 취소");
//
//        return ResponseEntity.ok(response);
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

    /**
     * 현재 요청 사용자 ID 추출
     * 실제 구현에서는 JWT 토큰이나 세션에서 추출
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        // TODO: JWT 토큰이나 인증 정보에서 사용자 ID 추출
        String userIdHeader = request.getHeader("X-User-Id");
        return userIdHeader != null ? Long.parseLong(userIdHeader) : null;
    }
}