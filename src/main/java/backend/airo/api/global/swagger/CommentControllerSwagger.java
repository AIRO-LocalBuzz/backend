package backend.airo.api.global.swagger;

import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.clutr_fatvl.dto.ClutrFatvInfoResponse;
import backend.airo.api.comment.dto.CommentCountResponse;
import backend.airo.api.comment.dto.CommentRequest;
import backend.airo.api.comment.dto.CommentResponse;
import backend.airo.api.global.dto.Response;
import backend.airo.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Comment", description = "댓글 API")
@SecurityRequirement(name = "BearerAuth")
public interface CommentControllerSwagger {

    @Operation(summary = "댓글 생성", description = "새로운 댓글을 생성합니다. ")
    @Parameters({
            @Parameter(name = "postId", description = "게시글 번호", example = "1"),
            @Parameter(name = "content", description = "댓글 내용", example = "좋습니다.")
    })
    @ApiResponse(
            responseCode = "200",
            description = "댓글 생성 성공",
            content = @Content(schema = @Schema(implementation = CommentResponse.class))
    )
    @PreAuthorize("isAuthenticated()")
    Response<CommentResponse> createComment(
            @UserPrincipal User user,
            @RequestBody CommentRequest request);


    @Operation(summary = "게시글 댓글 갯수 조회", description = "게시글의 댓글 총 갯수를 조회 합니다. ")
    @Parameters({
            @Parameter(name = "postId", description = "게시글 번호", example = "1")
    })
    @ApiResponse(
            responseCode = "200",
            description = "댓글 갯수 조회 성공",
            content = @Content(schema = @Schema(implementation = CommentCountResponse.class))
    )
    Response<CommentCountResponse> getCommandCount(
            @PathVariable Long postId
    );


    @Operation(summary = "게시글 댓글 List 조회", description = "게시글 댓글 List를 조회 합니다.")
    @Parameters({
            @Parameter(name = "postId", description = "게시글 번호", example = "1")
    })
    @ApiResponse(
            responseCode = "200",
            description = "댓글 갯수 조회 성공",
            content = @Content(schema = @Schema(implementation = CommentResponse.class))
    )
    Response<List<CommentResponse>> createComment(
            @PathVariable Long postId
    );

}
