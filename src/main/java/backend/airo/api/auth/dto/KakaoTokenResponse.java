package backend.airo.api.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoTokenResponse {
    private final String token_type;
    private final String access_token;
    private final String refresh_token;
    private final Integer expires_in;
    private final Integer refresh_token_expires_in;
}