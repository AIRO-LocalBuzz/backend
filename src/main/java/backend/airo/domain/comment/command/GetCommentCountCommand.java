package backend.airo.domain.comment.command;

import backend.airo.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetCommentCountCommand {

    private final CommentRepository commentRepository;

    public Long handle(Long postId) {
        return commentRepository.findCommentCount(postId);
    }

}
