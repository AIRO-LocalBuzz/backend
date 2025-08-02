package backend.airo.domain.user.command;

import backend.airo.domain.user.User;
import backend.airo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateExistingUserCommand {

    private final UserRepository userRepository;

    public User handle(Long userId, String name, String nickname, String phoneNumber, LocalDate birthDate) {
        User user = userRepository.findById(userId);
        User updateUser = user.updateUserInfo(name, nickname, phoneNumber, birthDate);
        return userRepository.save(updateUser);
    }
}