package backend.airo.domain.thumbnail.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.thumbnail.Thumbnail;
import java.util.Optional;

public interface ThumbnailRepository extends AggregateSupport<Thumbnail, Long> {
    Optional<Thumbnail> findByPostId(Long postId);
    void deleteByPostId(Long postId);
}
