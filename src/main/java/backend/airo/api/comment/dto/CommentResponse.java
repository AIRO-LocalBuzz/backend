package backend.airo.api.comment.dto;

public record CommentResponse(
        Long id,
        String content,
        Long postId,
        Long userId
) {
}
