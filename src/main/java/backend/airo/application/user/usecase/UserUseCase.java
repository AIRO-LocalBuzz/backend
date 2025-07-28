package backend.airo.application.user.usecase;

import backend.airo.domain.user.User;
import backend.airo.domain.user.command.UpdateExistingUserCommand;
import backend.airo.domain.user.query.GetUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUseCase {

    private final GetUserCommand getUserCommand;
    private final UpdateExistingUserCommand updateExistingUserCommand;

    public User getUserById(Long userId) {
        return getUserCommand.handle(userId);
    }

//    public User updateUser(Long userId, String nickname, String email) {
//
//    }

}
