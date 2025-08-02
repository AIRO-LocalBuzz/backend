package backend.airo.persistence.comment.repository;

import backend.airo.persistence.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findByPostId(Long postId);

    Long countByPostId(Long postId);

}
