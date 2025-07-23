package backend.airo.api.auth;

import backend.airo.api.auth.dto.AuthResponse;
import backend.airo.api.auth.dto.SocialLoginRequest;
import backend.airo.application.auth.service.SocialLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final SocialLoginService socialLoginService;

    /**
     * 소셜 로그인 (토큰 기반)
     */
    @PostMapping("/social-login")
    public ResponseEntity<AuthResponse> socialLogin(@RequestBody SocialLoginRequest request) {
        try {
            log.info("Social login attempt with provider: {}", request.getProvider());
            AuthResponse response = socialLoginService.socialLogin(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Unsupported provider: {}", request.getProvider());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Social login failed", e);
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * 구글 토큰 로그인
     */
    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleLogin(@RequestParam String token) {
        try {
            log.info("Google token login attempt");
            AuthResponse response = socialLoginService.loginWithGoogle(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Google login failed", e);
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * 카카오 토큰 로그인
     */
    @PostMapping("/kakao")
    public ResponseEntity<AuthResponse> kakaoLogin(@RequestParam String token) {
        try {
            log.info("Kakao token login attempt");
            AuthResponse response = socialLoginService.loginWithKakao(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Kakao login failed", e);
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        try {
            log.info("User logout attempt");
            // JWT는 stateless이므로 클라이언트에서 토큰 삭제
            // 필요시 블랙리스트 로직 추가
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Logout failed", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 토큰 갱신
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam String refreshToken) {
        try {
            log.info("Token refresh attempt");
            AuthResponse response = socialLoginService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Token refresh failed", e);
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * 토큰 유효성 검증
     */
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String token) {
        try {
            String cleanToken = token.replace("Bearer ", "");
            boolean isValid = socialLoginService.validateToken(cleanToken);
            return isValid ? ResponseEntity.ok().build() : ResponseEntity.status(401).build();
        } catch (Exception e) {
            log.error("Token validation failed", e);
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * 사용자 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            String cleanToken = token.replace("Bearer ", "");
            // 현재 사용자 정보 반환 로직 추가
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Get current user failed", e);
            return ResponseEntity.status(401).build();
        }
    }
}