package backend.airo.persistence.post.repository;

import backend.airo.domain.post.Post;
import backend.airo.domain.post.repository.PostRepository;
import backend.airo.domain.tag.Tag;
import backend.airo.persistence.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {
    Optional<PostEntity> findByTitle(String title);
    Optional<PostEntity> findById(Long id);
    Optional<PostEntity> findByContent(String content);
    Optional<PostEntity> findByAuthorId(Long authorId);
    Optional<PostEntity> findByAuthorEmail(String authorEmail);
    Optional<PostEntity> findByAuthorName(String authorName);
    Optional<PostEntity> findByPostTag(Tag tag);

}
