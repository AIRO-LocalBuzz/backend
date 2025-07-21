package backend.airo.domain.user.query;

import backend.airo.domain.user.UserProfile;
import backend.airo.domain.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllUserProfilesQuery {

    private final UserProfileRepository userProfileRepository;

    public List<UserProfile> handle() {
        return userProfileRepository.findAll();
    }
}