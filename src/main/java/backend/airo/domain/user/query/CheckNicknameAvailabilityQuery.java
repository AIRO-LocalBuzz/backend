package backend.airo.domain.user.query;

import backend.airo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckNicknameAvailabilityQuery {

    private final UserRepository userRepository;

    public boolean handle(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            return false;
        }
        
        if (nickname.length() < 2 || nickname.length() > 20) {
            return false;
        }
        
        // Allow only Korean, English, numbers, and some special characters
        if (!nickname.matches("^[가-힣a-zA-Z0-9._-]+$")) {
            return false;
        }
        
        return !userRepository.existsByNickname(nickname);
    }
}