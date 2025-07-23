package backend.airo.api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthTokenRequest {
    @NotBlank(message = "인증 코드는 필수입니다.")
    private final String code;
}