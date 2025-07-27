package backend.airo.domain.user.command;

import backend.airo.domain.auth.oauth2.OAuth2UserInfo;
import backend.airo.domain.user.User;
import backend.airo.domain.user.repository.UserRepository;
import backend.airo.domain.user.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserCommand {

    private final UserRepository userRepository;

    public User createNewUser(OAuth2UserInfo oauth2UserInfo, ProviderType providerType) {
        return userRepository.save(
            new User(
                    oauth2UserInfo.getEmail(),
                    oauth2UserInfo.getName(),
                    providerType,
                    oauth2UserInfo.getId()
            )
        );
    }
}
