package backend.airo.domain.user.command;

import backend.airo.domain.user.UserProfile;
import backend.airo.domain.user.exception.UserErrorCode;
import backend.airo.domain.user.exception.UserException;
import backend.airo.domain.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UpdateUserProfileCommand {

    private final UserProfileRepository userProfileRepository;

    public UserProfile handle(Long id, String name, String contact, LocalDate birthDate, String email) {
        UserProfile existingProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> UserException.notFound(UserErrorCode.USER_PROFILE_NOT_FOUND, "UpdateUserProfileCommand"));
        
        UserProfile updatedProfile = existingProfile.updateProfile(name, contact, birthDate, email);
        return userProfileRepository.save(updatedProfile);
    }
}