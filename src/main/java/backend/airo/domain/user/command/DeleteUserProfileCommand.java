package backend.airo.domain.user.command;

import backend.airo.domain.user.exception.UserErrorCode;
import backend.airo.domain.user.exception.UserException;
import backend.airo.domain.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserProfileCommand {

    private final UserProfileRepository userProfileRepository;

    public void handle(Long id) {
        if (userProfileRepository.findById(id).isEmpty()) {
            throw UserException.notFound(UserErrorCode.USER_PROFILE_NOT_FOUND, "DeleteUserProfileCommand");
        }
        userProfileRepository.deleteById(id);
    }
}