package backend.airo.persistence.comment.adapter;


import backend.airo.domain.comment.Comment;
import backend.airo.domain.comment.repository.CommentRepository;
import backend.airo.persistence.comment.entity.CommentEntity;
import backend.airo.persistence.comment.repository.CommentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentAdapter implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Comment save(Comment comment) {
        CommentEntity commentEntity = CommentEntity.toEntity(comment);
        CommentEntity saveCommentEntity = commentJpaRepository.save(commentEntity);
        return CommentEntity.toDomain(saveCommentEntity);
    }

    @Override
    public Collection<Comment> saveAll(Collection<Comment> aggregates) {
        return null;
    }

    @Override
    public Comment findById(Long aLong) {

        return null;
    }

    @Override
    public List<Comment> findAllByPostId(Long postId) {
        List<CommentEntity> commentEntities = commentJpaRepository.findByPostId(postId);
        return commentEntities.stream().map(CommentEntity::toDomain).toList();
    }
}
