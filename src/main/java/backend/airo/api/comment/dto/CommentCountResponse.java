package backend.airo.api.comment.dto;

public record CommentCountResponse(

        Long postId,
        Long commentCount

) {
}
