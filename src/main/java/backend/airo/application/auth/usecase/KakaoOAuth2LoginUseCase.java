package backend.airo.application.auth.usecase;

import backend.airo.api.auth.dto.AuthResponse;
import backend.airo.domain.auth.command.GenerateJwtTokenCommand;
import backend.airo.application.auth.oauth2.usecase.OAuth2UserProcessingUseCase;
import backend.airo.domain.auth.query.GetKakaoUserInfoQuery;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoOAuth2LoginUseCase {

    private final GetKakaoUserInfoQuery getKakaoUserInfoQuery;
    private final OAuth2UserProcessingUseCase oauth2UserProcessingUseCase;
    private final GenerateJwtTokenCommand generateJwtTokenCommand;

    public AuthResponse execute(String accessToken) {
        try {
            log.info("Kakao OAuth2 로그인 시작");

            // 1. Kakao 사용자 정보 조회
            Map<String, Object> kakaoUserInfo = getKakaoUserInfoQuery.execute(accessToken);

            // 2. 사용자 처리
            User user = oauth2UserProcessingUseCase.processOAuth2User(ProviderType.KAKAO, kakaoUserInfo);

            // 3. JWT 토큰 생성
            AuthResponse authResponse = generateJwtTokenCommand.execute(user);

            log.info("Kakao 로그인 성공 - User ID: {}, Email: {}", user.getId(), user.getEmail());
            return authResponse;

        } catch (Exception e) {
            log.error("Kakao 로그인 실패", e);
            throw new RuntimeException("Kakao 로그인에 실패했습니다.", e);
        }
    }
}