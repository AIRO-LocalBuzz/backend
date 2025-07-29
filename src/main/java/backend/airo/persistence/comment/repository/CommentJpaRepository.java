package backend.airo.persistence.comment.repository;

import backend.airo.persistence.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {
    // This interface extends JpaRepository to provide CRUD operations for CommentEntity.
    // Additional query methods can be defined here if needed.
}
