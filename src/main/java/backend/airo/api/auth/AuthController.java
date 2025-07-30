package backend.airo.api.auth;

import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.auth.dto.AuthResponse;
import backend.airo.api.auth.dto.AuthTokenRequest;
import backend.airo.api.auth.dto.SignInRequest;
import backend.airo.api.auth.dto.SocialLoginRequest;
import backend.airo.application.auth.usecase.AuthTokenUseCase;
import backend.airo.application.auth.usecase.RefreshTokenUseCase;
import backend.airo.application.auth.usecase.SignInUseCase;
import backend.airo.application.auth.usecase.SocialLoginUseCase;
import backend.airo.common.jwt.JwtTokenProvider;
import backend.airo.domain.auth.command.LogoutCommand;
import backend.airo.domain.auth.query.ValidateTokenQuery;
import backend.airo.domain.user.User;
import com.google.api.client.auth.oauth2.RefreshTokenRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final SocialLoginUseCase socialLoginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final ValidateTokenQuery validateTokenQuery;
    private final AuthTokenUseCase authTokenUseCase;
    private final SignInUseCase signInUseCase;
    private final LogoutCommand logoutCommand;

    /**
     * OAuth2 토큰 교환
     */
    @PostMapping("/exchange-token")
    public ResponseEntity<AuthResponse> exchangeToken(@Valid @RequestBody AuthTokenRequest request) {
        AuthResponse response = authTokenUseCase.exchangeToken(request.getCode());
        return ResponseEntity.ok(response);
    }



    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse>signUp(@Valid @RequestBody SignInRequest request) {
        AuthResponse response = signInUseCase.exchangeTokenForNewUser(request);
        return ResponseEntity.ok(response);
    }


    /**
     * 소셜 로그인 (토큰 기반)
     */
    @PostMapping("/social-login")
    public ResponseEntity<AuthResponse> socialLogin(@RequestBody SocialLoginRequest request) {
        AuthResponse response = socialLoginUseCase.execute(request);
        return ResponseEntity.ok(response);
    }


    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @UserPrincipal User user,
            @RequestHeader(value = "Authorization", required = true) String bearerToken) {
        if (!bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header format");
        }

        String accessToken = bearerToken.substring(7);
        if (accessToken.isEmpty()) {
            throw new IllegalArgumentException("Access token is empty");
        }

        logoutCommand.execute(accessToken, user.getId());

        return ResponseEntity.ok().build();
    }

    /**
     * 토큰 갱신
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @UserPrincipal User user,
            @RequestBody RefreshTokenRequest request) {
        AuthResponse response = refreshTokenUseCase.execute(user.getId(), request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    /**
     * 토큰 유효성 검증
     */
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String token) {
            String cleanToken = token.replace("Bearer ", "");
            boolean isValid = validateTokenQuery.execute(cleanToken);
            return isValid ? ResponseEntity.ok().build() : ResponseEntity.status(401).build();
    }

    /**
     * 사용자 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
            String cleanToken = token.replace("Bearer ", "");
            // Todo 현재 사용자 정보 반환 로직 추가
            return ResponseEntity.ok().build();
    }
}