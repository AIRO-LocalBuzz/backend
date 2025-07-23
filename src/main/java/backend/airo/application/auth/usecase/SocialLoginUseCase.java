package backend.airo.application.auth.usecase;

import backend.airo.api.auth.dto.AuthResponse;
import backend.airo.api.auth.dto.SocialLoginRequest;
import backend.airo.application.auth.oauth2.usecase.OAuth2UserProcessingUseCase;
import backend.airo.domain.auth.command.GenerateJwtTokenCommand;
import backend.airo.domain.auth.query.ValidateTokenQuery;
import backend.airo.persistence.user.entity.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SocialLoginUseCase {

    private final OAuth2UserProcessingUseCase oauth2UserProcessingUseCase;
    private final GenerateJwtTokenCommand generateJwtTokenCommand;
    private final ValidateTokenQuery validateTokenQuery;
    private final GoogleOAuth2LoginUseCase googleOAuth2LoginUseCase;
    private final KakaoOAuth2LoginUseCase kakaoOAuth2LoginUseCase;

    public AuthResponse execute(SocialLoginRequest request) {
        log.info("소셜 로그인 요청 - Provider: {}", request.getProvider());

        ProviderType providerType = getProviderType(request.getProvider());

        return switch (providerType) {
            case GOOGLE -> googleOAuth2LoginUseCase.execute(request.getToken());
            case KAKAO -> kakaoOAuth2LoginUseCase.execute(request.getToken());
            default -> throw new IllegalArgumentException("지원하지 않는 제공자: " + request.getProvider());
        };
    }

    private ProviderType getProviderType(String provider) {
        return switch (provider.toLowerCase()) {
            case "google" -> ProviderType.GOOGLE;
            case "kakao" -> ProviderType.KAKAO;
            default -> throw new IllegalArgumentException("지원하지 않는 제공자: " + provider);
        };
    }
}