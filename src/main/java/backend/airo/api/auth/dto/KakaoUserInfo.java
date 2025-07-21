package backend.airo.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfo {
    private String id;
    private String email;
    private String nickname;
    private String profileImageUrl;
}