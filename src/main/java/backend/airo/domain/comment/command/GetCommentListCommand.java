package backend.airo.domain.comment.command;

import backend.airo.domain.comment.Comment;
import backend.airo.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetCommentListCommand {

    private final CommentRepository commentRepository;

    public List<Comment> handle(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }
}
