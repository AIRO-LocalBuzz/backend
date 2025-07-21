package backend.airo.application.user.usecase;

import backend.airo.domain.user.UserProfile;
import backend.airo.domain.user.command.CreateUserProfileCommand;
import backend.airo.domain.user.command.DeleteUserProfileCommand;
import backend.airo.domain.user.command.UpdateUserProfileCommand;
import backend.airo.domain.user.query.GetAllUserProfilesQuery;
import backend.airo.domain.user.query.GetUserProfileQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserProfileUseCase {

    private final CreateUserProfileCommand createUserProfileCommand;
    private final UpdateUserProfileCommand updateUserProfileCommand;
    private final DeleteUserProfileCommand deleteUserProfileCommand;
    private final GetUserProfileQuery getUserProfileQuery;
    private final GetAllUserProfilesQuery getAllUserProfilesQuery;

    public UserProfile createUserProfile(String name, String contact, LocalDate birthDate, String email) {
        return createUserProfileCommand.handle(name, contact, birthDate, email);
    }

    public UserProfile getUserProfile(Long id) {
        return getUserProfileQuery.handle(id);
    }

    public List<UserProfile> getAllUserProfiles() {
        return getAllUserProfilesQuery.handle();
    }

    public UserProfile updateUserProfile(Long id, String name, String contact, LocalDate birthDate, String email) {
        return updateUserProfileCommand.handle(id, name, contact, birthDate, email);
    }

    public void deleteUserProfile(Long id) {
        deleteUserProfileCommand.handle(id);
    }
}