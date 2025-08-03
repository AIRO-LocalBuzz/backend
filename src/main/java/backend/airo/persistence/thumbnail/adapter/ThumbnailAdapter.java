package backend.airo.persistence.thumbnail.adapter;

import backend.airo.domain.thumbnail.Thumbnail;
import backend.airo.domain.thumbnail.repository.ThumbnailRepository;
import backend.airo.persistence.thumbnail.entity.ThumbnailEntity;
import backend.airo.persistence.thumbnail.repository.ThumbnailJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ThumbnailAdapter implements ThumbnailRepository {

    private final ThumbnailJpaRepository thumbnailJpaRepository;

    @Override
    public Thumbnail save(Thumbnail thumbnail) {
        ThumbnailEntity entity = ThumbnailEntity.toEntity(thumbnail);
        ThumbnailEntity saved = thumbnailJpaRepository.save(entity);
        return ThumbnailEntity.toDomain(saved);
    }

    @Override
    public Collection<Thumbnail> saveAll(Collection<Thumbnail> aggregates) {
        return List.of();
    }

    @Override
    public Thumbnail findById(Long id) {
        return thumbnailJpaRepository.findById(id)
                .map(ThumbnailEntity::toDomain)
                .orElse(null);
    }

    @Override
    public Optional<Thumbnail> findByPostId(Long postId) {
        return thumbnailJpaRepository.findByPostId(postId)
                .map(ThumbnailEntity::toDomain);
    }

    @Override
    public void deleteByPostId(Long postId) {
        thumbnailJpaRepository.deleteByPostId(postId);
    }
}