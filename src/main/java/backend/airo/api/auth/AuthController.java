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

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleLogin(@RequestParam String token) {
        try {
            log.info("Google login attempt");
            AuthResponse response = socialLoginService.loginWithGoogle(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Google login failed", e);
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/kakao")
    public ResponseEntity<AuthResponse> kakaoLogin(@RequestParam String token) {
        try {
            log.info("Kakao login attempt");
            AuthResponse response = socialLoginService.loginWithKakao(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Kakao login failed", e);
            return ResponseEntity.status(401).build();
        }
    }
}