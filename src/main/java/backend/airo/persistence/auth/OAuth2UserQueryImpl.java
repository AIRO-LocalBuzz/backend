package backend.airo.persistence.auth;

import backend.airo.domain.auth.oauth2.query.OAuth2UserQuery;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import backend.airo.persistence.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OAuth2UserQueryImpl implements OAuth2UserQuery {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByProviderIdAndProviderType(String providerId, ProviderType providerType) {
        return userRepository.findByProviderIdAndProviderType(providerId, providerType);
    }
}