package backend.airo.application.comment.usecase;

import backend.airo.domain.comment.Comment;
import backend.airo.domain.comment.command.GetCommentCountCommand;
import backend.airo.domain.comment.command.GetCommentListCommand;
import backend.airo.domain.comment.query.CreateCommentQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentUseCase {

    private final GetCommentListCommand getCommentListCommand;
    private final CreateCommentQuery createCommentQuery;
    private final GetCommentCountCommand getCommentCountCommand;


    public List<Comment> getCommentList(Long postId) {
        return getCommentListCommand.handle(postId);
    }

    public Comment createComment(String content, Long postId, Long userId) {
        return createCommentQuery.handle(content, postId, userId);
    }

    public Long getCommentCount(Long postId) {
        return getCommentCountCommand.handle(postId);
    }
}
