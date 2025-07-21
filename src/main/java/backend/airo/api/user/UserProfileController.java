package backend.airo.api.user;

import backend.airo.api.global.dto.Response;
import backend.airo.api.user.dto.CreateUserProfileRequest;
import backend.airo.api.user.dto.UpdateUserProfileRequest;
import backend.airo.api.user.dto.UserProfileResponse;
import backend.airo.application.user.usecase.UserProfileUseCase;
import backend.airo.domain.user.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileUseCase userProfileUseCase;

    @PostMapping
    public Response<UserProfileResponse> createUserProfile(@RequestBody CreateUserProfileRequest request) {
        UserProfile userProfile = userProfileUseCase.createUserProfile(
                request.name(),
                request.contact(),
                request.birthDate(),
                request.email()
        );
        return Response.success(UserProfileResponse.from(userProfile));
    }

    @GetMapping("/{id}")
    public Response<UserProfileResponse> getUserProfile(@PathVariable Long id) {
        UserProfile userProfile = userProfileUseCase.getUserProfile(id);
        return Response.success(UserProfileResponse.from(userProfile));
    }

    @GetMapping
    public Response<List<UserProfileResponse>> getAllUserProfiles() {
        List<UserProfile> userProfiles = userProfileUseCase.getAllUserProfiles();
        List<UserProfileResponse> responses = userProfiles.stream()
                .map(UserProfileResponse::from)
                .toList();
        return Response.success(responses);
    }

    @PutMapping("/{id}")
    public Response<UserProfileResponse> updateUserProfile(
            @PathVariable Long id,
            @RequestBody UpdateUserProfileRequest request) {
        UserProfile userProfile = userProfileUseCase.updateUserProfile(
                id,
                request.name(),
                request.contact(),
                request.birthDate(),
                request.email()
        );
        return Response.success(UserProfileResponse.from(userProfile));
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteUserProfile(@PathVariable Long id) {
        userProfileUseCase.deleteUserProfile(id);
        return Response.success(null);
    }
}