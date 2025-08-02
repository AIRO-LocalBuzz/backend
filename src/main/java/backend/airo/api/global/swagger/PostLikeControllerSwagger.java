package backend.airo.api.global.swagger;

import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.clutr_fatvl.dto.ClutrFatvInfoResponse;
import backend.airo.api.global.dto.Response;
import backend.airo.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface PostLikeControllerSwagger {


    @Operation(summary = "게시글 좋아요 업데이트", description = "게시글 좋아요 업데이트 API")
    @Parameters({
            @Parameter(name = "postId", description = "게시글 번호", example = "1")
    })
    @ApiResponse(
            responseCode = "200",
            description = "게시글 좋아요 업데이트 성공"
    )
    Response<Void> updatePostLike(
            @Parameter(description = "게시물 ID", required = true)
            @UserPrincipal User user,
            @PathVariable Long postId);


    @Operation(summary = "게시글 좋아요 삭제", description = "게시글 좋아요 삭제 API")
    @Parameters({
            @Parameter(name = "postId", description = "게시글 번호", example = "1")
    })
    @ApiResponse(
            responseCode = "200",
            description = "게시글 좋아요 삭제 성공"
    )
    public Response<Void> deletePostLike(
            @UserPrincipal User user,
            @PathVariable Long postId);

}
