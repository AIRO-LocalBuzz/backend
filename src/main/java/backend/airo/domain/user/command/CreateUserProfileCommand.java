package backend.airo.domain.user.command;

import backend.airo.domain.user.UserProfile;
import backend.airo.domain.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CreateUserProfileCommand {

    private final UserProfileRepository userProfileRepository;

    public UserProfile handle(String name, String contact, LocalDate birthDate, String email) {
        UserProfile userProfile = new UserProfile(name, contact, birthDate, email);
        return userProfileRepository.save(userProfile);
    }
}