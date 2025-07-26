package backend.airo.domain.auth.query;

import backend.airo.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidateTokenQuery {

    private final JwtTokenProvider jwtTokenProvider;

    public boolean execute(String token) {
        try {
            return jwtTokenProvider.validateToken(token);
        } catch (Exception e) {
            log.error("토큰 검증 실패", e);
            return false;
        }
    }
}