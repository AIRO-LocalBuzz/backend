package backend.airo.domain.comment.query;

import backend.airo.domain.comment.Comment;
import backend.airo.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class CreateCommentQuery {

    private final CommentRepository commentRepository;

    public Comment handle(String content, Long postId, Long userId) {
        return commentRepository.save(
                new Comment(
                        0L,
                        content,
                        postId,
                        userId
                )
        );
    }

}
