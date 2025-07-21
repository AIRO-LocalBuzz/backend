package backend.airo.application.user.usecase;

import backend.airo.domain.user.User;
import backend.airo.domain.user.command.CreateUserCommand;
import backend.airo.domain.user.command.UpdateNicknameCommand;
import backend.airo.domain.user.query.CheckNicknameAvailabilityQuery;
import backend.airo.domain.user.query.GetUserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUseCase {

    private final CreateUserCommand createUserCommand;
    private final UpdateNicknameCommand updateNicknameCommand;
    private final CheckNicknameAvailabilityQuery checkNicknameAvailabilityQuery;
    private final GetUserQuery getUserQuery;

    public boolean checkNicknameAvailability(String nickname) {
        return checkNicknameAvailabilityQuery.handle(nickname);
    }

    public User createUser(String nickname) {
        return createUserCommand.handle(nickname);
    }

    public User updateNickname(Long userId, String nickname) {
        return updateNicknameCommand.handle(userId, nickname);
    }

    public User getUser(Long userId) {
        return getUserQuery.handle(userId);
    }
}