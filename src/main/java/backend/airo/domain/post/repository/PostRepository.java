package backend.airo.domain.post.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.post.Post;

public interface PostRepository extends AggregateSupport<Post, Long> {
//    Optional<Post> findByProviderIdAndProviderType(String providerId, ProviderType providerType);


    Post findById(Long id);

}
