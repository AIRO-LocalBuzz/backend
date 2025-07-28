package backend.airo.application.user.usecase;

import backend.airo.domain.user.User;
import backend.airo.domain.user.command.UpdateExistingUserCommand;
import backend.airo.domain.user.query.GetUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UserUseCase {

    private final GetUserCommand getUserCommand;
    private final UpdateExistingUserCommand updateExistingUserCommand;

    public User getUserById(Long userId) {
        return getUserCommand.handle(userId);
    }

    public User updateUser(Long userId, String name, String nickname, String phoneNumber, LocalDate birthDate) {
        return updateExistingUserCommand.handle(userId, name, nickname, phoneNumber, birthDate);
    }

}
