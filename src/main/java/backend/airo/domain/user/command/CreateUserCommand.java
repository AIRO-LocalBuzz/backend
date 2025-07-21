package backend.airo.domain.user.command;

import backend.airo.domain.user.User;
import backend.airo.domain.user.repository.UserRepository;
import backend.airo.domain.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserCommand {

    private final UserRepository userRepository;

    public User handle(String nickname) {
        validateNickname(nickname);
        
        if (userRepository.existsByNickname(nickname)) {
            throw UserException.nicknameAlreadyExists("CreateUserCommand");
        }
        
        return userRepository.save(new User(nickname));
    }
    
    private void validateNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw UserException.nicknameInvalid("CreateUserCommand");
        }
        
        if (nickname.length() < 2 || nickname.length() > 20) {
            throw UserException.nicknameInvalid("CreateUserCommand");
        }
        
        // Allow only Korean, English, numbers, and some special characters
        if (!nickname.matches("^[가-힣a-zA-Z0-9._-]+$")) {
            throw UserException.nicknameInvalid("CreateUserCommand");
        }
    }
}