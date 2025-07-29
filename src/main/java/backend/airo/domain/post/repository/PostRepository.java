package backend.airo.domain.post.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.post.Post;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends AggregateSupport<Post, Long> {
//    Optional<Post> findByProviderIdAndProviderType(String providerId, ProviderType providerType);


    Post findById(Long id);

}
