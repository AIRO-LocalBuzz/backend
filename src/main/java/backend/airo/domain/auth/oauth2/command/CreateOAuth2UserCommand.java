package backend.airo.domain.auth.oauth2.command;

import backend.airo.domain.auth.oauth2.OAuth2UserInfo;
import backend.airo.domain.user.User;
import backend.airo.domain.user.repository.UserRepository;
import backend.airo.domain.user.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateOAuth2UserCommand {
    private final UserRepository userRepository;

    public User execute(Optional<User> existingUser, OAuth2UserInfo oauth2UserInfo, ProviderType providerType) {
        return existingUser
                .orElseGet(() -> createNewUser(oauth2UserInfo, providerType));
    }

    private User createNewUser(OAuth2UserInfo oauth2UserInfo, ProviderType providerType) {
        log.info("새로운 OAuth2 사용자 생성 - Email: {}, Provider: {}", oauth2UserInfo.getEmail(), providerType);

        User newUser = User.createNewUser(
                oauth2UserInfo.getEmail(),
                oauth2UserInfo.getName(),
                providerType,
                oauth2UserInfo.getId()
        );

        User savedUser = userRepository.save(newUser);
        log.info("새로운 사용자 생성 완료 - User ID: {}", savedUser.getId());

        return savedUser;
    }


}