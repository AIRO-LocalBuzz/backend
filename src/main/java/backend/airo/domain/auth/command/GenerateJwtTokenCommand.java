package backend.airo.domain.auth.command;

import backend.airo.api.auth.dto.AuthResponse;
import backend.airo.common.jwt.JwtTokenProvider;
import backend.airo.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerateJwtTokenCommand {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenValidityInDays;

    public AuthResponse execute(User user) {
        log.info("JWT 토큰 생성 - User ID: {}", user.getId());

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        redisTemplate.opsForValue().set(
                "RT:" + user.getId(),
                refreshToken,
                refreshTokenValidityInDays,
                TimeUnit.DAYS
        );

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenValidityInSeconds())
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}