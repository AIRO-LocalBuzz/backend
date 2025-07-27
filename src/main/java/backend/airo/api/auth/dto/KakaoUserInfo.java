package backend.airo.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoUserInfo {
    private final String id;
    private final String email;
    private final String nickname;
    private final String profileImageUrl;
}