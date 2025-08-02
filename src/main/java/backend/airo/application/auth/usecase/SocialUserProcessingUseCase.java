package backend.airo.application.auth.usecase;

import backend.airo.domain.auth.oauth2.OAuth2UserInfo;
import backend.airo.domain.auth.oauth2.OAuth2UserInfoFactory;
import backend.airo.domain.auth.oauth2.command.CreateOAuth2UserCommand;
import backend.airo.domain.auth.oauth2.query.FindOAuth2UserQuery;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocialUserProcessingUseCase {

    private final FindOAuth2UserQuery findOAuth2UserQuery;
    private final CreateOAuth2UserCommand createOAuth2UserCommand;

    @Transactional
    public User processSocialUser(ProviderType providerType, Map<String, Object> attributes) {
        log.info("OAuth2 사용자 처리 시작 - Provider: {}", providerType);
        String email = (String) attributes.get("email");

        // 1. OAuth2 정보 추출 및 검증
        OAuth2UserInfo oauth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, attributes);
        validateUserInfo(oauth2UserInfo, providerType);

        // 2. 기존 사용자 이메일로 조회
        Optional<User> existingUser = findOAuth2UserQuery.findByEmail(email);


        if (!existingUser.isPresent()) {
            User newUser = createOAuth2UserCommand.execute(existingUser, oauth2UserInfo, providerType);
            log.info("새로운 사용자 생성 완료 - User ID: {}", newUser.getId());
            return newUser;
        }
        else {
            User user = existingUser.get();
            return user;
        }
    }

    private void validateUserInfo(OAuth2UserInfo oauth2UserInfo, ProviderType providerType) {
        if (providerType == ProviderType.GOOGLE) {
            if (oauth2UserInfo.getEmail() == null || oauth2UserInfo.getEmail().isEmpty()) {
                throw new IllegalArgumentException("Email not found from OAuth2 provider");
            }
        }
    }
}