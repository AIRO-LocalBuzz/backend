package backend.airo.persistence.post.adapter;

import backend.airo.domain.post.Post;
import backend.airo.domain.post.repository.PostRepository;
import backend.airo.persistence.post.entity.PostEntity;
import backend.airo.persistence.post.repository.PostJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Transactional
@RequiredArgsConstructor
public class PostAdapter implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    @Override
    public Post save(Post post) {
        // Convert Post to PostEntity and save it using PostJpaRepository
        // Return the saved Post as a domain model
        return null; // Placeholder return statement
    }

    @Override
    public Collection<Post> saveAll(Collection<Post> posts) {
        // Convert each Post to PostEntity and save them using PostJpaRepository
        // Return the saved Posts as domain models
        return null; // Placeholder return statement
    }

    @Override
    public Post findById(Long id) {

        PostEntity postEntity = postJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post Not Found with id - " + id));

        return PostEntity.toDomain(postEntity);
    }
}
