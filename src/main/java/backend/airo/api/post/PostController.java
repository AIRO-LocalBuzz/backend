package backend.airo.api.post;

import backend.airo.application.post.usecase.PostUseCase;
import backend.airo.domain.post.command.CreatePostCommand;
import backend.airo.domain.post.command.DeletePostCommand;
import backend.airo.domain.post.command.UpdatePostCommand;
import backend.airo.domain.post.query.GetPostListQuery;
import backend.airo.domain.post.query.GetPostQuery;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.api.post.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Post Controller - 게시물 관련 REST API
 */
@Slf4j
@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시물 관리 API")
public class PostController {

    private final PostUseCase postUseCase;

    // ===== 게시물 생성 =====

    @Operation(summary = "게시물 생성", description = "새로운 게시물을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "게시물 생성 성공",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403",
                    description = "발행 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody PostCreateRequest request,
            HttpServletRequest httpRequest) {

        log.info("게시물 생성 요청: title={}, userId={}", request.title(), request.userId());

        CreatePostCommand command = new CreatePostCommand(
                request.title(),
                request.content(),
                request.userId(),
                request.status() != null ? request.status() : PostStatus.DRAFT,
                request.categoryId(),
                request.locationId(),
                request.travelDate(),
                request.imageIds(),
                request.tags(),
                request.isFeatured()
        );

        Post createdPost = postUseCase.createPost(command);
        PostResponse response = PostResponse.fromDomain(createdPost);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ===== 게시물 조회 =====

    @Operation(summary = "게시물 단건 조회", description = "ID로 특정 게시물을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "게시물 조회 성공",
                    content = @Content(schema = @Schema(implementation = PostDetailResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "게시물을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403",
                    description = "접근 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostDetailResponse> getPost(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable Long id,
            @Valid @ModelAttribute PostDetailRequest request,
            HttpServletRequest httpRequest) {

        Long requesterId = getCurrentUserId(httpRequest);

        GetPostQuery query = new GetPostQuery(
                id,
                requesterId,
                request.includeImages(),
                request.includeTags(),
                request.includeComments(),
                true, // includeAuthor
                true, // includeLocation
                true, // includeCategory
                request.incrementViewCount()
        );

        Post post = postUseCase.getPostById(query);
        PostDetailResponse response = PostDetailResponse.fromDomain(post);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시물 목록 조회", description = "조건에 따라 게시물 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "게시물 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = PostListResponse.class)))
    })
    @GetMapping
    public ResponseEntity<PostListResponse> getPostList(
            @Valid @ModelAttribute PostListRequest request,
            HttpServletRequest httpRequest) {

        Long requesterId = getCurrentUserId(httpRequest);

        GetPostListQuery query = new GetPostListQuery(
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortDirection(),
                request.statuses(),
                request.userId(),
                request.categoryId(),
                request.locationId(),
                request.tags(),
                request.isFeatured(),
                null, // startDate
                null, // endDate
                request.keyword(),
                request.searchScope(),
                requesterId,
                true, // includeImages
                true, // includeTags
                true  // includeAuthor
        );

        Page<Post> postPage = postUseCase.getPostList(query);
        PostListResponse response = PostListResponse.fromDomain(postPage);

        return ResponseEntity.ok(response);
    }

    // ===== 게시물 수정 =====

    @Operation(summary = "게시물 수정", description = "기존 게시물을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "게시물 수정 성공",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403",
                    description = "수정 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "게시물을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest request,
            HttpServletRequest httpRequest) {

        Long requesterId = getCurrentUserId(httpRequest);

        UpdatePostCommand command = new UpdatePostCommand(
                id,
                requesterId,
                request.title(),
                request.content(),
                request.status(),
                request.categoryId(),
                request.locationId(),
                request.travelDate(),
                request.imageIds(),
                request.tags(),
                request.isFeatured(),
                request.changeReason()
        );

        Post updatedPost = postUseCase.updatePost(command);
        PostResponse response = PostResponse.fromDomain(updatedPost);

        return ResponseEntity.ok(response);
    }

    // ===== 게시물 삭제 =====

    @Operation(summary = "게시물 삭제", description = "게시물을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "게시물 삭제 성공"),
            @ApiResponse(responseCode = "403",
                    description = "삭제 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "게시물을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable Long id,
            @Valid @ModelAttribute PostDeleteRequest request,
            HttpServletRequest httpRequest) {

        Long requesterId = getCurrentUserId(httpRequest);

        DeletePostCommand command = new DeletePostCommand(
                id,
                requesterId,
                request.deleteReason(),
                request.forceDelete()
        );

        postUseCase.deletePost(command);

        return ResponseEntity.noContent().build();
    }

    // ===== 게시물 좋아요 =====

    @Operation(summary = "게시물 좋아요/취소", description = "게시물에 좋아요를 추가하거나 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "좋아요 처리 성공",
                    content = @Content(schema = @Schema(implementation = LikeResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "게시물을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{id}/like")
    public ResponseEntity<LikeResponse> likePost(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable Long id,
            HttpServletRequest request) {

        Long userId = getCurrentUserId(request);
        boolean isLiked = postUseCase.likePost(id, userId);

        LikeResponse response = new LikeResponse(isLiked, isLiked ? "좋아요 추가" : "좋아요 취소");

        return ResponseEntity.ok(response);
    }

    // ===== 통계 API =====

    @Operation(summary = "인기 게시물 조회", description = "조회수 기준 인기 게시물을 조회합니다.")
    @GetMapping("/popular")
    public ResponseEntity<List<PostResponse>> getPopularPosts(
            @Valid @ModelAttribute PopularPostsRequest request) {

        List<Post> popularPosts = postUseCase.getPopularPosts(request.limit());
        List<PostResponse> responses = popularPosts.stream()
                .map(PostResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "최근 인기 게시물 조회", description = "최근 기간 내 인기 게시물을 조회합니다.")
    @GetMapping("/recent-popular")
    public ResponseEntity<List<PostResponse>> getRecentPopularPosts(
            @Valid @ModelAttribute RecentPopularPostsRequest request) {

        List<Post> recentPopularPosts = postUseCase.getRecentPopularPosts(request.days(), request.limit());
        List<PostResponse> responses = recentPopularPosts.stream()
                .map(PostResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(responses);
    }

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