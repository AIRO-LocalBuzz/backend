package backend.airo.domain.user.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.user.User;
import backend.airo.persistence.user.entity.ProviderType;

import java.util.Optional;

public interface UserRepository extends AggregateSupport<User, Long> {
    Optional<User> findByProviderIdAndProviderType(String providerId, ProviderType providerType);

    User findById(Long id);

}
