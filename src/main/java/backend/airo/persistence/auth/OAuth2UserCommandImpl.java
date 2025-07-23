package backend.airo.persistence.auth;

import backend.airo.domain.auth.oauth2.command.OAuth2UserCommand;
import backend.airo.domain.user.User;
import backend.airo.persistence.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OAuth2UserCommandImpl implements OAuth2UserCommand {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}