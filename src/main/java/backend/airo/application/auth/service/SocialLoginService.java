package backend.airo.application.auth.service;

import backend.airo.api.auth.dto.AuthResponse;
import backend.airo.api.auth.dto.KakaoUserInfo;
import backend.airo.api.auth.dto.SocialLoginRequest;
import backend.airo.common.jwt.JwtTokenProvider;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import backend.airo.persistence.user.UserRepository;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SocialLoginService {

    private final FirebaseAuthService firebaseAuthService;
    private final KakaoAuthService kakaoAuthService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse loginWithGoogle(String idToken) {
        try {
            FirebaseToken firebaseToken = firebaseAuthService.verifyToken(idToken);
            
            String email = firebaseToken.getEmail();
            String name = firebaseToken.getName();
            String profileImageUrl = firebaseToken.getPicture();
            String providerId = firebaseToken.getUid();

            User user = userRepository.findByEmailAndProviderType(email, ProviderType.GOOGLE)
                    .orElseGet(() -> createUser(email, name, profileImageUrl, ProviderType.GOOGLE, providerId));

            // Update profile if changed
            if (!user.getName().equals(name) || 
                (profileImageUrl != null && !profileImageUrl.equals(user.getProfileImageUrl()))) {
                user.updateProfile(name, profileImageUrl);
            }

            String accessToken = jwtTokenProvider.generateToken(user.getId(), user.getEmail());

            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getProfileImageUrl(),
                    "google"
            );

            return new AuthResponse(accessToken, userInfo);

        } catch (FirebaseAuthException e) {
            log.error("Firebase token verification failed", e);
            throw new RuntimeException("Invalid Google token", e);
        }
    }

    public AuthResponse loginWithKakao(String accessToken) {
        try {
            KakaoUserInfo kakaoUserInfo = kakaoAuthService.getUserInfo(accessToken);

            User user = userRepository.findByProviderIdAndProviderType(kakaoUserInfo.getId(), ProviderType.KAKAO)
                    .orElseGet(() -> createUser(
                            kakaoUserInfo.getEmail(),
                            kakaoUserInfo.getNickname(),
                            kakaoUserInfo.getProfileImageUrl(),
                            ProviderType.KAKAO,
                            kakaoUserInfo.getId()
                    ));

            // Update profile if changed
            if (!user.getName().equals(kakaoUserInfo.getNickname()) ||
                (kakaoUserInfo.getProfileImageUrl() != null && !kakaoUserInfo.getProfileImageUrl().equals(user.getProfileImageUrl()))) {
                user.updateProfile(kakaoUserInfo.getNickname(), kakaoUserInfo.getProfileImageUrl());
            }

            String jwtToken = jwtTokenProvider.generateToken(user.getId(), user.getEmail());

            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getProfileImageUrl(),
                    "kakao"
            );

            return new AuthResponse(jwtToken, userInfo);

        } catch (Exception e) {
            log.error("Kakao login failed", e);
            throw new RuntimeException("Invalid Kakao token", e);
        }
    }

    public AuthResponse socialLogin(SocialLoginRequest request) {
        switch (request.getProvider().toLowerCase()) {
            case "google":
                return loginWithGoogle(request.getToken());
            case "kakao":
                return loginWithKakao(request.getToken());
            default:
                throw new IllegalArgumentException("Unsupported provider: " + request.getProvider());
        }
    }

    private User createUser(String email, String name, String profileImageUrl, ProviderType providerType, String providerId) {
        User user = new User(email, name, profileImageUrl, providerType, providerId);
        return userRepository.save(user);
    }
}