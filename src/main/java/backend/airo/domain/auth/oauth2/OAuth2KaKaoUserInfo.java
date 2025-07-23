package backend.airo.domain.auth.oauth2;

import java.util.Map;

public abstract class OAuth2KaKaoUserInfo {
    protected Map<String, Object> attributes;

    public OAuth2KaKaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();
    public abstract String getName();
}