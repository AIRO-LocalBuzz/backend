package backend.airo.domain.user.command;

import backend.airo.domain.user.User;
import backend.airo.domain.user.repository.UserRepository;
import backend.airo.domain.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateNicknameCommand {

    private final UserRepository userRepository;

    public User handle(Long userId, String nickname) {
        validateNickname(nickname);
        
        // Check if nickname is already taken by someone else
        userRepository.findByNickname(nickname)
            .filter(existingUser -> !existingUser.getId().equals(userId))
            .ifPresent(existingUser -> {
                throw UserException.nicknameAlreadyExists("UpdateNicknameCommand");
            });
        
        User user = userRepository.findById(userId);
        if (user == null) {
            throw UserException.userNotFound("UpdateNicknameCommand");
        }
        
        User updatedUser = user.updateNickname(nickname);
        return userRepository.save(updatedUser);
    }
    
    private void validateNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw UserException.nicknameInvalid("UpdateNicknameCommand");
        }
        
        if (nickname.length() < 2 || nickname.length() > 20) {
            throw UserException.nicknameInvalid("UpdateNicknameCommand");
        }
        
        // Allow only Korean, English, numbers, and some special characters
        if (!nickname.matches("^[가-힣a-zA-Z0-9._-]+$")) {
            throw UserException.nicknameInvalid("UpdateNicknameCommand");
        }
    }
}