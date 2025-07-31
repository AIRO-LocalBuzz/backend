package backend.airo.domain.auth.oauth2;

import backend.airo.domain.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {
    private final Map<String, Object> attributes;
    private final User user;

    public CustomOAuth2User(Map<String, Object> attributes, User user) {
        // 기존 속성에 User 정보 추가
        this.attributes = new HashMap<>(attributes);
        this.attributes.put("provider_id", user.getProviderId());
        this.attributes.put("provider_type", user.getProviderType());
        this.attributes.put("user_id", user.getId());
        this.attributes.put("email", user.getEmail());
        this.attributes.put("name", user.getName());

        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return user.getName();
    }

    public User getUser() {
        return user;
    }
}