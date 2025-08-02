package backend.airo.api.post;

import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.global.dto.Response;
import backend.airo.api.global.swagger.PostLikeControllerSwagger;
import backend.airo.application.post.usecase.PostLikeUserCase;
import backend.airo.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PostLikeController implements PostLikeControllerSwagger {

    private final PostLikeUserCase postLikeUserCase;


    @Override
    @PostMapping("/post/like/{postId}")
    public Response<Void> updatePostLike(
            @UserPrincipal User user,
            @PathVariable Long postId) {
        postLikeUserCase.upsertPostLike(postId, user.getId());
        return Response.success();
    }

    @Override
    @DeleteMapping("/post/like/{postId}")
    public Response<Void> deletePostLike(
            @UserPrincipal User user,
            @PathVariable Long postId) {
        postLikeUserCase.deletePostLike(postId, user.getId());
        return Response.success();
    }

}
