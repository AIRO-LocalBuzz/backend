package backend.airo.domain.auth.oauth2;

import backend.airo.domain.user.enums.ProviderType;

import java.util.Map;

public class OAuth2KaKaoUserInfoFactory {
    public static OAuth2KaKaoUserInfo getOAuth2KaKaoUserInfo(ProviderType providerType, Map<String, Object> attributes) {
        return switch (providerType) {
            case KAKAO -> new KakaoOAuth2UserInfo(attributes);
            default -> throw new IllegalArgumentException("Invalid Provider Type: " + providerType);
        };
    }
}