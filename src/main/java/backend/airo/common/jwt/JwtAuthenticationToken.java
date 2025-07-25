package backend.airo.common.jwt;

import backend.airo.domain.user.User;
import backend.airo.persistence.user.entity.UserEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final User principal;
    private final Object credentials;

    public JwtAuthenticationToken(User principal, Object credentials,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public User getPrincipal() {
        return principal;
    }
}