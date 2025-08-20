package backend.airo.api.user.dto;

import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record UserResponse(
        Long id,
        String email,
        String name,
        String nickname,
        String phoneNumber,
        LocalDate birthDate,
        ProviderType providerType,
        String providerId,
        LocalDateTime lastLoginAt,
        LocalDateTime createdAt

) {
    public static UserResponse create(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .providerType(user.getProviderType())
                .providerId(user.getProviderId())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
