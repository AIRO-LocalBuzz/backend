package backend.airo.application.auth.oauth2;

import backend.airo.api.auth.dto.AuthTokenRequest;
import backend.airo.api.auth.dto.AuthTokenResponse;
import backend.airo.common.jwt.JwtTokenProvider;
import backend.airo.domain.auth.oauth2.query.OAuth2UserQuery;
import backend.airo.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2UserQuery oauth2UserQuery;

    public AuthTokenResponse exchangeToken(AuthTokenRequest request) {
        log.info("토큰 교환 요청 - 임시 코드: {}", request.getCode());

        // Redis에서 임시 코드로 사용자 ID 조회
        String userIdStr = redisTemplate.opsForValue().get("auth_code:" + request.getCode());

        if (userIdStr == null) {
            log.warn("유효하지 않거나 만료된 인증 코드: {}", request.getCode());
            throw new IllegalArgumentException("유효하지 않거나 만료된 인증 코드입니다.");
        }

        Long userId = Long.parseLong(userIdStr);
        log.info("Redis에서 사용자 ID 조회 성공: {}", userId);

        // 사용자 정보 조회
        Optional<User> userOptional = oauth2UserQuery.findById(userId);
        if (userOptional.isEmpty()) {
            log.error("사용자를 찾을 수 없음 - User ID: {}", userId);
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        User user = userOptional.get();
        log.info("사용자 정보 조회 성공 - id: {}", user.getId());

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(userId);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId);

        log.info("JWT 토큰 생성 완료 - User ID: {}", userId);

        // Redis에서 임시 코드 삭제 (일회용)
        redisTemplate.delete("auth_code:" + request.getCode());
        log.info("임시 코드 삭제 완료: {}", request.getCode());

        // 응답 생성
        AuthTokenResponse response = new AuthTokenResponse(
                accessToken,
                refreshToken,
                "Bearer",
                jwtTokenProvider.getAccessTokenValidityInSeconds(),
                user.getId(),
                user.getNickname()
        );

        log.info("토큰 교환 성공 - User ID: {}", userId);
        return response;
    }
}