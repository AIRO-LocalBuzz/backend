package backend.airo.application.auth.usecase;

import backend.airo.api.auth.dto.AuthResponse;
import backend.airo.domain.auth.query.ValidateTokenQuery;
import backend.airo.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenUseCase {

    private final JwtTokenProvider jwtTokenProvider;
    private final ValidateTokenQuery validateTokenQuery;

    public AuthResponse execute(String refreshToken) {
        try {
            // 1. 토큰 유효성 검증
            if (!validateTokenQuery.execute(refreshToken)) {
                throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
            }

            // 2. 사용자 ID 추출
            Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

            // 3. 새로운 토큰 생성
            String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);

            log.info("토큰 갱신 완료 - User ID: {}", userId);

            return AuthResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtTokenProvider.getAccessTokenValidityInSeconds())
                    .userId(userId)
                    .build();

        } catch (Exception e) {
            log.error("토큰 갱신 실패", e);
            throw new RuntimeException("토큰 갱신에 실패했습니다.", e);
        }
    }
}