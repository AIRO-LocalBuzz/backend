package backend.airo.api.user.dto;

import backend.airo.domain.user.UserProfile;

import java.time.LocalDate;

public record UserProfileResponse(
        Long id,
        String name,
        String contact,
        LocalDate birthDate,
        String email
) {
    public static UserProfileResponse from(UserProfile userProfile) {
        return new UserProfileResponse(
                userProfile.getId(),
                userProfile.getName(),
                userProfile.getContact(),
                userProfile.getBirthDate(),
                userProfile.getEmail()
        );
    }
}