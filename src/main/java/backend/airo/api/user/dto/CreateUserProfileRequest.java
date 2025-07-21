package backend.airo.api.user.dto;

import java.time.LocalDate;

public record CreateUserProfileRequest(
        String name,
        String contact,
        LocalDate birthDate,
        String email
) {}