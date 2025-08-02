package backend.airo.api.global.swagger;

import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.comment.dto.CommentRequest;
import backend.airo.api.comment.dto.CommentResponse;
import backend.airo.api.global.dto.Response;
import backend.airo.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Comment", description = "댓글 API")
@SecurityRequirement(name = "BearerAuth")
public interface CommentControllerSwagger {

    @Operation(summary = "댓글 생성", description = "새로운 댓글을 생성합니다. ")
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    Response<CommentResponse> createComment(
            @UserPrincipal User user,
            @RequestBody CommentRequest request);


}
