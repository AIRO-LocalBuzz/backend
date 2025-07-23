package backend.airo.application.auth.usecase;

import backend.airo.api.auth.dto.AuthResponse;
import backend.airo.domain.auth.command.GenerateJwtTokenCommand;
import backend.airo.application.auth.oauth2.usecase.OAuth2UserProcessingUseCase;
import backend.airo.application.auth.service.FirebaseAuthService;
import backend.airo.domain.user.User;
import backend.airo.persistence.user.entity.ProviderType;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuth2LoginUseCase {

    private final FirebaseAuthService firebaseAuthService;
    private final OAuth2UserProcessingUseCase oauth2UserProcessingUseCase;
    private final GenerateJwtTokenCommand generateJwtTokenCommand;

    public AuthResponse execute(String idToken) {
        try {
            log.info("Google OAuth2 로그인 시작");

            // 1. Firebase 토큰 검증
            FirebaseToken decodedToken = firebaseAuthService.verifyToken(idToken);

            // 2. OAuth2 사용자 정보 구성
            Map<String, Object> attributes = buildGoogleAttributes(decodedToken);

            // 3. 사용자 처리
            User user = oauth2UserProcessingUseCase.processOAuth2User(ProviderType.GOOGLE, attributes);

            // 4. JWT 토큰 생성
            AuthResponse authResponse = generateJwtTokenCommand.execute(user);

            log.info("Google 로그인 성공 - User ID: {}, Email: {}", user.getId(), user.getEmail());
            return authResponse;

        } catch (Exception e) {
            log.error("Google 로그인 실패", e);
            throw new RuntimeException("Google 로그인에 실패했습니다.", e);
        }
    }

    private Map<String, Object> buildGoogleAttributes(FirebaseToken decodedToken) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", decodedToken.getUid());
        attributes.put("email", decodedToken.getEmail());
        attributes.put("name", decodedToken.getName());
        attributes.put("picture", decodedToken.getPicture());
        return attributes;
    }
}