package backend.airo.application.auth.oauth2.usecase;

import backend.airo.domain.auth.oauth2.command.GenerateTempCodeCommand;
import backend.airo.domain.auth.oauth2.query.FindOAuth2UserQuery;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationUseCase {

    private final FindOAuth2UserQuery findOAuth2UserQuery;
    private final GenerateTempCodeCommand generateTempCodeCommand;

    @Value("${app.oauth2.success-url:http://localhost:3000/auth/success}")
    private String successBaseUrl;

    @Value("${app.oauth2.failure-url:http://localhost:3000/auth/failure}")
    private String failureUrl;

    public String handleAuthenticationSuccess(OAuth2User oauth2User, String accessToken) {
        // 1. OAuth2 정보 추출
        String providerId = oauth2User.getAttribute("provider_id");
        ProviderType providerType = oauth2User.getAttribute("provider_type");

        log.info("Provider ID: {}, Provider Type: {}", providerId, providerType);

        // 2. 사용자 조회
        Optional<User> userOptional = findOAuth2UserQuery.findByProviderIdAndType(providerId, providerType);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            log.info("기존 사용자 찾음 - User ID: {}, Email: {}", user.getId(), user.getEmail());

            // 3. 토큰 저장
            generateTempCodeCommand.generate(user.getId(), accessToken);

            // 4. 성공 URL 반환
            return successBaseUrl + "?token=" + accessToken;

        } else {
            log.warn("사용자를 찾을 수 없음 - Provider ID: {}, Provider Type: {}", providerId, providerType);
            return failureUrl;
        }
    }
}