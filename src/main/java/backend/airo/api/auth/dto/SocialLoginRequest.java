package backend.airo.api.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SocialLoginRequest {
    private final String token;
    private final String provider; // "google" or "kakao"
}