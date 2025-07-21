package backend.airo.api.user.dto;

import java.time.LocalDate;

public record UpdateUserProfileRequest(
        String name,
        String contact,
        LocalDate birthDate,
        String email
) {}