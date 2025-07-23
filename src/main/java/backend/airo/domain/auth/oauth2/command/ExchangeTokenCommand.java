package backend.airo.domain.auth.oauth2.command;

import backend.airo.api.auth.dto.AuthTokenResponse;
import backend.airo.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExchangeTokenCommand {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public AuthTokenResponse execute(Long userId, String tempCode) {

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(userId);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId);

        // 임시 코드 삭제
        redisTemplate.delete("auth_code:" + tempCode);

        return new AuthTokenResponse(accessToken, refreshToken, "Bearer",
                jwtTokenProvider.getAccessTokenValidityInSeconds(),
                userId);
    }
}