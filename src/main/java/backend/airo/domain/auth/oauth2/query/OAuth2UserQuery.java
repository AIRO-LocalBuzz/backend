package backend.airo.domain.auth.oauth2.query;

import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;

import java.util.Optional;

public interface OAuth2UserQuery {
    Optional<User> findByProviderIdAndProviderType(String providerId, ProviderType providerType);

    Optional<User> findById(Long id);
}
