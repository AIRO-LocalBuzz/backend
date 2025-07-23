// backend/airo/application/auth/oauth2/OAuth2UserProcessingService.java
package backend.airo.application.auth.oauth2;

import backend.airo.domain.auth.oauth2.OAuth2KaKaoUserInfo;
import backend.airo.domain.auth.oauth2.OAuth2UserInfo;
import backend.airo.domain.auth.oauth2.OAuth2UserInfoFactory;
import backend.airo.domain.auth.oauth2.OAuth2KaKaoUserInfoFactory;
import backend.airo.domain.auth.oauth2.command.OAuth2UserCommand;
import backend.airo.domain.auth.oauth2.query.OAuth2UserQuery;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserProcessingService {

    private final OAuth2UserQuery oauth2UserQuery;
    private final OAuth2UserCommand oauth2UserCommand;

    @Transactional
    public User processOAuth2User(ProviderType providerType, Map<String, Object> attributes) {

        OAuth2UserInfo oauth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, attributes);

        validateUserInfo(oauth2UserInfo);

        Optional<User> existingUser = oauth2UserQuery.findByProviderIdAndProviderType(
                oauth2UserInfo.getId(), providerType);

        return existingUser
                .map(user -> updateExistingUser(user, oauth2UserInfo))
                .orElseGet(() -> createNewUser(oauth2UserInfo, providerType));
    }

//    @Transactional
//    public User processOAuth2KaKaoUser(ProviderType providerType, Map<String, Object> attributes) {
//
//        OAuth2KaKaoUserInfo oauth2UserInfo = OAuth2KaKaoUserInfoFactory.getOAuth2KaKaoUserInfo(providerType, attributes);
//
////        validateUserInfo(oauth2UserInfo);
//
//        Optional<User> existingUser = oauth2UserQuery.findByProviderIdAndProviderType(
//                oauth2UserInfo.getId(), providerType);
//
//        return existingUser
//                .map(user -> updateExistingUser(user, oauth2UserInfo))
//                .orElseGet(() -> createNewUser(oauth2UserInfo, providerType));
//    }

    private void validateUserInfo(OAuth2UserInfo oauth2UserInfo) {
        if (oauth2UserInfo.getEmail() == null || oauth2UserInfo.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email not found from OAuth2 provider");
        }
    }

    private User createNewUser(OAuth2UserInfo oauth2UserInfo, ProviderType providerType) {
        User user = User.createOAuth2User(
                oauth2UserInfo.getEmail(),
                oauth2UserInfo.getName(),
                providerType,
                oauth2UserInfo.getId() // 이게 Google의 "sub" 값
        );

        log.info("Creating new user with provider: {}, id: {}", providerType, oauth2UserInfo.getId());
        return oauth2UserCommand.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oauth2UserInfo) {
        existingUser.updateProfile(oauth2UserInfo.getName());
        log.info("Updating existing user: {}", existingUser.getId());
        return oauth2UserCommand.save(existingUser);
    }


}