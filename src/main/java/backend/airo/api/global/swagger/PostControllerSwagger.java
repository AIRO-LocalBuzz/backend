package backend.airo.api.global.swagger;


import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.global.dto.Response;
import backend.airo.api.post.dto.*;
import backend.airo.domain.post.Post;
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
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Slice;
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
            @ApiResponse(responseCode = "400 | 401",
                    description = "잘못된 요청 데이터 | 권한 없음",
                    content = @Content(schema = @Schema(implementation = Response.class))),
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    Response<PostResponse> createPost(
            @Valid @RequestBody PostCreateRequest request,
            @UserPrincipal User user);



    @Operation(summary = "게시물 단건 조회", description = "ID로 특정 게시물을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "게시물 조회 성공",
                    content = @Content(schema = @Schema(implementation = PostDetailResponse.class))),
            @ApiResponse(responseCode = "404 | 401",
                    description = "게시물을 찾을 수 없음 | 권한 없음",
                    content = @Content(schema = @Schema(implementation = Response.class))),
    })
    @GetMapping("/{id}")
    Response<PostDetailResponse> getPost(
            @Parameter(description = "게시물 ID", required = true)
            @UserPrincipal User user,
            @PathVariable @Positive Long postId);



    @Operation(summary = "썸네일 조회", description = "썸네일 ID로 썸네일을 조회합니다.")
    @GetMapping("/{thumbnailId}")
    Response<ThumbnailResponseDto> getThumbnail(
            @Parameter(description = "썸네일 ID", required = true)
            @UserPrincipal User user,
            @PathVariable @Positive Long thumbnailId);



    @Operation(summary = "게시물 목록 조회", description = "조건에 따라 게시물 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "게시물 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = PostListResponse.class)))
    })
    @GetMapping
    Response<PostListResponse> getPostList(
            @Valid @ModelAttribute PostListRequest request);



    @Operation(summary = "게시물 스크롤 조회", description = "최신순 스크롤 게시물 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "게시물 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = PostListResponse.class)))
    })
    @GetMapping
    Response<PostSliceResponse> getPostSlice(
            @Valid @ModelAttribute PostSliceRequest request);




    @Operation(summary = "게시물 수정", description = "기존 게시물을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "게시물 수정 성공",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400 | 401 | 404",
                    description = "잘못된 요청 데이터 | 권한 없음 | 게시물을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = Response.class))),
    })
    @PutMapping("/{id}")
    Response<PostResponse> updatePost(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable @Positive Long id,
            @Valid @RequestBody PostUpdateRequest request,
            @UserPrincipal User user);



    @Operation(summary = "게시물 삭제", description = "게시물을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "게시물 삭제 성공"),
            @ApiResponse(responseCode = "401 | 404",
                    description = "권한 없음 | 게시물을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = Response.class))),
    })
    @DeleteMapping("/{id}")
    Response<Void> deletePost(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable @Positive Long id,
            @UserPrincipal User user);






//    @Operation(summary = "썸네일과 함께 게시물 생성", description = "새로운 게시물과 썸네일을 생성합니다. (운영용-테스트금지")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201",
//                    description = "게시물 생성 성공",
//                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
//            @ApiResponse(responseCode = "400 | 401",
//                    description = "잘못된 요청 데이터 | 권한 없음",
//                    content = @Content(schema = @Schema(implementation = Response.class))),
//    })
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/thumbnail")
//    Response<PostResponse> createPostAndThumbnail(
//            @Valid @RequestBody PostCreateRequest request,
//            @UserPrincipal User user);




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
