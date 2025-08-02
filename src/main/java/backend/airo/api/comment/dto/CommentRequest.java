package backend.airo.api.comment.dto;

public record CommentRequest(
        String content,
        Long postId
) {
}
