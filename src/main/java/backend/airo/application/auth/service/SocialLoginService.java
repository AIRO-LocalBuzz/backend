package backend.airo.application.auth.service;

import backend.airo.api.auth.dto.AuthResponse;
import backend.airo.api.auth.dto.SocialLoginRequest;
import backend.airo.application.auth.oauth2.OAuth2UserProcessingService;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import backend.airo.common.jwt.JwtTokenProvider;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SocialLoginService {

    private final FirebaseAuthService firebaseAuthService;
    private final OAuth2UserProcessingService oauth2UserProcessingService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;


    public AuthResponse socialLogin(SocialLoginRequest request) {
        ProviderType providerType = getProviderType(request.getProvider());

        return switch (providerType) {
            case GOOGLE -> loginWithGoogle(request.getToken());
            case KAKAO -> loginWithKakao(request.getToken());
            default -> throw new IllegalArgumentException("Unsupported provider: " + request.getProvider());
        };
    }

    public AuthResponse loginWithGoogle(String idToken) {
        try {
            FirebaseToken decodedToken = firebaseAuthService.verifyToken(idToken);

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("sub", decodedToken.getUid());
            attributes.put("email", decodedToken.getEmail());
            attributes.put("name", decodedToken.getName());
            attributes.put("picture", decodedToken.getPicture());

            User user = oauth2UserProcessingService.processOAuth2User(ProviderType.GOOGLE, attributes);

            String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

            log.info("Google login successful for user: {}", user.getEmail());

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtTokenProvider.getAccessTokenValidityInSeconds())
                    .userId(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();

        } catch (Exception e) {
            log.error("Google login failed", e);
            throw new RuntimeException("Google login failed", e);
        }
    }

    public AuthResponse loginWithKakao(String accessToken) {
        try {
            Map<String, Object> kakaoUserInfo = getKakaoUserInfo(accessToken);

            User user = oauth2UserProcessingService.processOAuth2User(ProviderType.KAKAO, kakaoUserInfo);

            String jwtAccessToken = jwtTokenProvider.generateAccessToken(user.getId());
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

            log.info("Kakao login successful for user: {}", user.getEmail());

            return AuthResponse.builder()
                    .accessToken(jwtAccessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtTokenProvider.getAccessTokenValidityInSeconds())
                    .userId(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();

        } catch (Exception e) {
            log.error("Kakao login failed", e);
            throw new RuntimeException("Kakao login failed", e);
        }
    }

    public AuthResponse refreshAccessToken(String refreshToken) {
        try {
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                throw new RuntimeException("Invalid refresh token");
            }

            Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
            String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);

            log.info("Token refreshed for user: {}", userId);

            return AuthResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtTokenProvider.getAccessTokenValidityInSeconds())
                    .userId(userId)
                    .build();

        } catch (Exception e) {
            log.error("Token refresh failed", e);
            throw new RuntimeException("Token refresh failed", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            return jwtTokenProvider.validateToken(token);
        } catch (Exception e) {
            log.error("Token validation failed", e);
            return false;
        }
    }

    private Map<String, Object> getKakaoUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null) {
                throw new RuntimeException("Failed to get Kakao user info");
            }

            // Kakao API 응답을 OAuth2UserInfo 형식으로 변환
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("id", responseBody.get("id"));

            Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");
            if (kakaoAccount != null) {
                attributes.put("email", kakaoAccount.get("email"));

                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                if (profile != null) {
                    attributes.put("nickname", profile.get("nickname"));
                    attributes.put("profile_image_url", profile.get("profile_image_url"));
                }
            }

            return attributes;

        } catch (Exception e) {
            log.error("Failed to get Kakao user info", e);
            throw new RuntimeException("Failed to get Kakao user info", e);
        }
    }



    private ProviderType getProviderType(String provider) {
        return switch (provider.toLowerCase()) {
            case "google" -> ProviderType.GOOGLE;
            case "kakao" -> ProviderType.KAKAO;
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };
    }
}