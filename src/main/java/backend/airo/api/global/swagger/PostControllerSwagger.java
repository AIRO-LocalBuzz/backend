package backend.airo.api.global.swagger;


import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.post.dto.*;
import backend.airo.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post", description = "게시물 관리 API")
@SecurityRequirement(name = "BearerAuth")
public interface PostControllerSwagger {
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
    @PreAuthorize("isAuthenticated()")
    @PostMapping
   ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody PostCreateRequest request,
            @UserPrincipal User user);




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
    ResponseEntity<PostDetailResponse> getPost(
            @Parameter(description = "게시물 ID", required = true)
            @UserPrincipal User user,
            @PathVariable Long postId);



    @Operation(summary = "게시물 목록 조회", description = "조건에 따라 게시물 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "게시물 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = PostListResponse.class)))
    })
    @GetMapping
    ResponseEntity<PostListResponse> getPostList(
            @Valid @ModelAttribute PostListRequest request);




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
    ResponseEntity<PostResponse> updatePost(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest request,
            @UserPrincipal User user);



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
    ResponseEntity<Void> deletePost(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable Long id,
            @UserPrincipal User user);


//
//
//
//    @Operation(summary = "인기 게시물 조회", description = "조회수 기준 인기 게시물을 조회합니다.")
//    @GetMapping("/popular")
// ResponseEntity<List<PostResponse>> getPopularPosts(
//            @Valid @ModelAttribute PopularPostsRequest request);
//
//
//
//
//
//    @Operation(summary = "최근 인기 게시물 조회", description = "최근 기간 내 인기 게시물을 조회합니다.")
//    @GetMapping("/recent-popular")
//    ResponseEntity<List<PostResponse>> getRecentPopularPosts(
//            @Valid @ModelAttribute RecentPopularPostsRequest request);
}
