package backend.airo.domain.user.query;

import backend.airo.domain.user.UserProfile;
import backend.airo.domain.user.exception.UserErrorCode;
import backend.airo.domain.user.exception.UserException;
import backend.airo.domain.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserProfileQuery {

    private final UserProfileRepository userProfileRepository;

    public UserProfile handle(Long id) {
        return userProfileRepository.findById(id)
                .orElseThrow(() -> UserException.notFound(UserErrorCode.USER_PROFILE_NOT_FOUND, "GetUserProfileQuery"));
    }
}