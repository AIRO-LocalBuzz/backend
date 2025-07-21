package backend.airo.domain.user.query;

import backend.airo.domain.user.User;
import backend.airo.domain.user.repository.UserRepository;
import backend.airo.domain.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserQuery {

    private final UserRepository userRepository;

    public User handle(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw UserException.userNotFound("GetUserQuery");
        }
        return user;
    }
}