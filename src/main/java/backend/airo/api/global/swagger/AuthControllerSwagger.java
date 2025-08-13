package backend.airo.api.global.swagger;

import backend.airo.api.annotation.JwtTokenParsing;
import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.auth.dto.AuthResponse;
import backend.airo.api.auth.dto.AuthTokenRequest;
import backend.airo.api.auth.dto.SignInRequest;
import backend.airo.api.auth.dto.SocialLoginRequest;
import backend.airo.api.global.dto.Response;
import backend.airo.domain.user.User;
import com.google.api.client.auth.oauth2.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "인증 관리 API")
public interface AuthControllerSwagger {

    @Operation(summary = "OAuth2 토큰 교환", description = "OAuth2 인가 코드를 JWT 토큰으로 교환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "토큰 교환 성공",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "잘못된 인가 코드 (AUTH_001: 유효하지 않은 코드)",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "401",
                    description = "인증 실패 (AUTH_002: OAuth2 인증 실패)",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping("/exchange-token")
    ResponseEntity<AuthResponse> exchangeToken(@Valid @RequestBody AuthTokenRequest request);

    @Operation(summary = "회원가입", description = "새로운 사용자 회원가입을 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "잘못된 요청 데이터 (USER_001: 필수 필드 누락, USER_002: 이메일 형식 오류)",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "409",
                    description = "중복 사용자 (USER_003: 이미 존재하는 낙네임)",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping("/sign-up")
    ResponseEntity<AuthResponse> signUp(@Valid @RequestBody SignInRequest request);

    @Operation(summary = "소셜 로그인", description = "소셜 플랫폼 토큰을 사용하여 로그인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "소셜 로그인 성공",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "잘못된 소셜 토큰 (AUTH_003: 유효하지 않은 소셜 토큰)",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "401",
                    description = "소셜 인증 실패 (AUTH_004: 소셜 플랫폼 인증 실패)",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping("/social-login")
    ResponseEntity<AuthResponse> socialLogin(@RequestBody SocialLoginRequest request);

    @Operation(summary = "로그아웃", description = "사용자 로그아웃 처리 및 토큰 무효화")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401",
                    description = "인증 토큰 오류 (AUTH_005: 유효하지 않은 토큰)",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping("/logout")
    ResponseEntity<Void> logout(
            @UserPrincipal User user,
            @JwtTokenParsing String accessToken);

    @Operation(summary = "토큰 갱신", description = "Refresh Token을 사용하여 새로운 Access Token 발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "토큰 갱신 성공",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "잘못된 Refresh Token (AUTH_006: 유효하지 않은 리프레시 토큰)",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "401",
                    description = "리프레시 토큰 만료 (AUTH_007: 만료된 리프레시 토큰)",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping("/refresh")
    ResponseEntity<AuthResponse> refreshToken(
            @UserPrincipal User user,
            @RequestBody RefreshTokenRequest request);

    @Operation(summary = "토큰 유효성 검증", description = "Access Token의 유효성을 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유효한 토큰"),
            @ApiResponse(responseCode = "401",
                    description = "무효한 토큰 (AUTH_008: 토큰 검증 실패)",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/validate")
    ResponseEntity<Void> validateToken(
            @Parameter(description = "검증할 Access Token", hidden = true)
            @JwtTokenParsing String accessToken);

    @Operation(summary = "현재 사용자 정보 조회", description = "인증된 사용자의 프로필 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
            @ApiResponse(responseCode = "401",
                    description = "인증 필요 (AUTH_009: 인증되지 않은 사용자)",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404",
                    description = "사용자 정보 없음 (USER_004: 사용자를 찾을 수 없음)",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/me")
    ResponseEntity<?> getCurrentUser(
            @Parameter(description = "사용자 Access Token", hidden = true)
            @JwtTokenParsing String accessToken);
}