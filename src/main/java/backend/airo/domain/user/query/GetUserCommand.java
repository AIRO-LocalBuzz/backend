package backend.airo.domain.user.query;

import backend.airo.domain.user.User;
import backend.airo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserCommand {

    private final UserRepository userRepository;

    public User handle(Long userId) {
        return userRepository.findById(userId);
    }

}
