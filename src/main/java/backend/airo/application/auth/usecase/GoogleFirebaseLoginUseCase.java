package backend.airo.application.auth.usecase;

import backend.airo.api.auth.dto.AuthResponse;
import backend.airo.application.auth.service.FirebaseAuthService;
import backend.airo.domain.auth.command.GenerateJwtTokenCommand;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleFirebaseLoginUseCase {


    private final FirebaseAuthService firebaseAuthService;
    private final SocialUserProcessingUseCase socialUserProcessingUseCase;
    private final GenerateJwtTokenCommand generateJwtTokenCommand;

    public AuthResponse execute(String idToken) {
        try {
            log.info("Google Firebase 로그인 시작");

            // 1. Firebase ID Token 검증
            FirebaseToken decodedToken = firebaseAuthService.verifyToken(idToken);

            // 2. Firebase Token에서 사용자 정보 추출
            Map<String, Object> attributes = extractUserAttributes(decodedToken);

            // 3. 사용자 처리 (생성 또는 업데이트)
            User user = socialUserProcessingUseCase.processSocialUser(ProviderType.GOOGLE, attributes);

            // 4. 자체 JWT 토큰 생성
            AuthResponse authResponse = generateJwtTokenCommand.execute(user);

            log.info("Google 로그인 성공 - User ID: {}, Email: {}", user.getId(), user.getEmail());
            return authResponse;

        } catch (Exception e) {
            log.error("Google 로그인 실패", e);
            throw new RuntimeException("Google 로그인에 실패했습니다.", e);
        }
    }

    private Map<String, Object> extractUserAttributes(FirebaseToken decodedToken) {
        Map<String, Object> attributes = new HashMap<>();

        // Firebase Token에서 사용자 정보 추출
        attributes.put("sub", decodedToken.getUid());        // Google 고유 ID
        attributes.put("email", decodedToken.getEmail());    // 이메일
        attributes.put("name", decodedToken.getName());      // 이름
        attributes.put("picture", decodedToken.getPicture()); // 프로필 이미지
        attributes.put("email_verified", decodedToken.isEmailVerified()); // 이메일 인증 여부

        log.info("Google 사용자 정보 추출 완료 - Email: {}, Name: {}",
                decodedToken.getEmail(), decodedToken.getName());

        return attributes;
    }
}