package backend.airo.domain.auth.oauth2.query;

import backend.airo.domain.user.User;
import backend.airo.domain.user.repository.UserRepository;
import backend.airo.persistence.user.entity.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FindOAuth2UserQuery {

    private final UserRepository userRepository;

    public Optional<User> findByProviderIdAndType(String providerId, ProviderType providerType) {
        log.info("OAuth2 사용자 조회 - Provider ID: {}, Type: {}", providerId, providerType);

        Optional<User> user = userRepository.findByProviderIdAndProviderType(providerId, providerType);

        if (user.isPresent()) {
            log.info("기존 OAuth2 사용자 발견 - User ID: {}", user.get().getId());
        } else {
            log.info("신규 OAuth2 사용자");
        }

        return user;
    }


    public User findById(Long id) {
        log.info("user id로 사용자 조회 - user ID: {}", id);

        User user = userRepository.findById(id);

        return user;
    }
}