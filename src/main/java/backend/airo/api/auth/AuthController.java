package backend.airo.api.auth;

import backend.airo.api.auth.dto.AuthResponse;
import backend.airo.api.auth.dto.AuthTokenRequest;
import backend.airo.api.auth.dto.AuthTokenResponse;
import backend.airo.api.auth.dto.SocialLoginRequest;
import backend.airo.application.auth.oauth2.AuthTokenService;
import backend.airo.application.auth.service.SocialLoginService;
import backend.airo.common.jwt.JwtTokenProvider;
import backend.airo.domain.auth.oauth2.query.OAuth2UserQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import backend.airo.domain.user.User;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final SocialLoginService socialLoginService;
    private final AuthTokenService authTokenService;

    /**
     * OAuth2 토큰 교환
     */
    @PostMapping("/exchange-token")
    public ResponseEntity<AuthTokenResponse> exchangeToken(@Valid @RequestBody AuthTokenRequest request) {
        AuthTokenResponse response = authTokenService.exchangeToken(request);
        return ResponseEntity.ok(response);
    }


    /**
     * 소셜 로그인 (토큰 기반)
     */
    @PostMapping("/social-login")
    public ResponseEntity<AuthResponse> socialLogin(@RequestBody SocialLoginRequest request) {
            log.info("Social login attempt with provider: {}", request.getProvider());
            AuthResponse response = socialLoginService.socialLogin(request);
            return ResponseEntity.ok(response);
    }

    /**
     * 구글 토큰 로그인
     */
    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleLogin(@RequestParam String token) {
            log.info("Google token login attempt");
            AuthResponse response = socialLoginService.loginWithGoogle(token);
            return ResponseEntity.ok(response);
    }

    /**
     * 카카오 토큰 로그인
     */
    @PostMapping("/kakao")
    public ResponseEntity<AuthResponse> kakaoLogin(@RequestParam String token) {
            log.info("Kakao token login attempt");
            AuthResponse response = socialLoginService.loginWithKakao(token);
            return ResponseEntity.ok(response);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
            log.info("User logout attempt");
            // JWT는 stateless이므로 클라이언트에서 토큰 삭제
            // 필요시 블랙리스트 로직 추가
            return ResponseEntity.ok().build();
    }

    /**
     * 토큰 갱신
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam String refreshToken) {
            log.info("Token refresh attempt");
            AuthResponse response = socialLoginService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(response);
    }

    /**
     * 토큰 유효성 검증
     */
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String token) {
            String cleanToken = token.replace("Bearer ", "");
            boolean isValid = socialLoginService.validateToken(cleanToken);
            return isValid ? ResponseEntity.ok().build() : ResponseEntity.status(401).build();
    }

    /**
     * 사용자 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
            String cleanToken = token.replace("Bearer ", "");
            // 현재 사용자 정보 반환 로직 추가
            return ResponseEntity.ok().build();
    }
}