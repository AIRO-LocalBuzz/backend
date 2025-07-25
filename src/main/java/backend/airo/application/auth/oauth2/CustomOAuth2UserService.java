package backend.airo.application.auth.oauth2;

import backend.airo.application.auth.oauth2.usecase.OAuth2UserProcessingUseCase;
import backend.airo.domain.auth.oauth2.CustomOAuth2User;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuth2UserProcessingUseCase oauth2UserProcessingUseCase;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        try {
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            ProviderType providerType = getProviderType(registrationId);

            User user = oauth2UserProcessingUseCase.processOAuth2User(providerType, oauth2User.getAttributes());

            return new CustomOAuth2User(oauth2User.getAttributes(), user);
        } catch (Exception e) {
            log.error("OAuth2 user processing failed", e);
            throw new OAuth2AuthenticationException("OAuth2 processing failed");
        }
    }


    protected OAuth2User callSuperLoadUser(OAuth2UserRequest userRequest) {
        return super.loadUser(userRequest);
    }

    protected ProviderType getProviderType(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> ProviderType.GOOGLE;
            case "kakao" -> ProviderType.KAKAO;
            default -> throw new IllegalArgumentException("지원하지 않는 OAuth2 제공자입니다: " + registrationId);
        };
    }
}