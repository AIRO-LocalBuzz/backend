package backend.airo.api.comment;

import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.comment.dto.CommentRequest;
import backend.airo.api.comment.dto.CommentResponse;
import backend.airo.api.global.dto.Response;
import backend.airo.api.global.swagger.CommentControllerSwagger;
import backend.airo.application.comment.usecase.CommentUseCase;
import backend.airo.domain.comment.Comment;
import backend.airo.domain.user.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "게시글 댓글 API V1")
public class CommentController implements CommentControllerSwagger {

    private final CommentUseCase commentUseCase;

    @Override
    @PostMapping("/comment")
    public Response<CommentResponse> createComment(
            @UserPrincipal User user,
            @RequestBody CommentRequest request
    ) {
        Comment comment = commentUseCase.createComment(request.content(), request.postId(), user.getId());
        return Response.success(new CommentResponse(comment.getId(), comment.getContent(), comment.getPostId(), comment.getUserId()));
    }

}
