package backend.airo.domain.comment.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.comment.Comment;

import java.util.List;

public interface CommentRepository extends AggregateSupport<Comment, Long> {

    List<Comment> findAllByPostId(Long postID);

}
