package backend.airo.application.auth.oauth2;

import backend.airo.api.auth.dto.AuthTokenRequest;
import backend.airo.api.auth.dto.AuthTokenResponse;
import backend.airo.common.jwt.JwtTokenProvider;
import backend.airo.domain.auth.oauth2.query.OAuth2UserQuery;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthTokenServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private OAuth2UserQuery oauth2UserQuery;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private AuthTokenService authTokenService;

    private User mockUser;
    private AuthTokenRequest request;

    @BeforeEach
    void setUp() {
        mockUser = User.createOAuth2User(
                "test@example.com",
                "Test User",
                ProviderType.GOOGLE,
                "google123"
        );

        request = new AuthTokenRequest("test-auth-code");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void exchangeToken_정상_토큰_교환_성공() {
        // given
        String authCode = "test-auth-code";
        Long userId = 1L;
        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        long validityInSeconds = 3600L;

        when(valueOperations.get("auth_code:" + authCode)).thenReturn(userId.toString());
        when(oauth2UserQuery.findById(userId)).thenReturn(Optional.of(mockUser));
        when(jwtTokenProvider.generateAccessToken(userId)).thenReturn(accessToken);
        when(jwtTokenProvider.generateRefreshToken(userId)).thenReturn(refreshToken);
        when(jwtTokenProvider.getAccessTokenValidityInSeconds()).thenReturn(validityInSeconds);

        AuthTokenRequest request = new AuthTokenRequest(authCode);

        // when
        AuthTokenResponse result = authTokenService.exchangeToken(request);

        // then
        assertNotNull(result);
        assertEquals(accessToken, result.getAccessToken());
        assertEquals(refreshToken, result.getRefreshToken());
        assertEquals("Bearer", result.getTokenType());
        assertEquals(validityInSeconds, result.getExpiresIn());
        assertEquals(mockUser.getId(), result.getUserId());
        assertEquals(mockUser.getNickname(), result.getNickname());

        verify(valueOperations).get("auth_code:" + authCode);
        verify(oauth2UserQuery).findById(userId);
        verify(jwtTokenProvider).generateAccessToken(userId);
        verify(jwtTokenProvider).generateRefreshToken(userId);
        verify(jwtTokenProvider).getAccessTokenValidityInSeconds();
        verify(redisTemplate).delete("auth_code:" + authCode);
    }

    @Test
    void exchangeToken_유효하지_않은_인증_코드_예외_발생() {
        // given
        String invalidAuthCode = "invalid-auth-code";

        when(valueOperations.get("auth_code:" + invalidAuthCode)).thenReturn(null);

        AuthTokenRequest request = new AuthTokenRequest(invalidAuthCode);

        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authTokenService.exchangeToken(request)
        );

        assertEquals("유효하지 않거나 만료된 인증 코드입니다.", exception.getMessage());

        verify(valueOperations).get("auth_code:" + invalidAuthCode);
        verify(oauth2UserQuery, never()).findById(any());
        verify(jwtTokenProvider, never()).generateAccessToken(any());
        verify(jwtTokenProvider, never()).generateRefreshToken(any());
        verify(redisTemplate, never()).delete(anyString());
    }

    @Test
    void exchangeToken_만료된_인증_코드_예외_발생() {
        // given
        String expiredAuthCode = "expired-auth-code";

        when(valueOperations.get("auth_code:" + expiredAuthCode)).thenReturn(null);

        AuthTokenRequest request = new AuthTokenRequest(expiredAuthCode);

        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authTokenService.exchangeToken(request)
        );

        assertEquals("유효하지 않거나 만료된 인증 코드입니다.", exception.getMessage());

        verify(valueOperations).get("auth_code:" + expiredAuthCode);
        verify(oauth2UserQuery, never()).findById(any());
        verify(jwtTokenProvider, never()).generateAccessToken(any());
        verify(jwtTokenProvider, never()).generateRefreshToken(any());
        verify(redisTemplate, never()).delete(anyString());
    }

    @Test
    void exchangeToken_사용자_없음_예외_발생() {
        // given
        String authCode = "test-auth-code";
        Long userId = 999L;

        when(valueOperations.get("auth_code:" + authCode)).thenReturn(userId.toString());
        when(oauth2UserQuery.findById(userId)).thenReturn(Optional.empty());

        AuthTokenRequest request = new AuthTokenRequest(authCode);

        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authTokenService.exchangeToken(request)
        );

        assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());

        verify(valueOperations).get("auth_code:" + authCode);
        verify(oauth2UserQuery).findById(userId);
        verify(jwtTokenProvider, never()).generateAccessToken(any());
        verify(jwtTokenProvider, never()).generateRefreshToken(any());
        verify(redisTemplate, never()).delete(anyString());
    }

    @Test
    void exchangeToken_Redis에서_임시_코드_삭제_확인() {
        // given
        String authCode = "test-auth-code";
        Long userId = 1L;
        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        long validityInSeconds = 3600L;

        when(valueOperations.get("auth_code:" + authCode)).thenReturn(userId.toString());
        when(oauth2UserQuery.findById(userId)).thenReturn(Optional.of(mockUser));
        when(jwtTokenProvider.generateAccessToken(userId)).thenReturn(accessToken);
        when(jwtTokenProvider.generateRefreshToken(userId)).thenReturn(refreshToken);
        when(jwtTokenProvider.getAccessTokenValidityInSeconds()).thenReturn(validityInSeconds);

        AuthTokenRequest request = new AuthTokenRequest(authCode);

        // when
        authTokenService.exchangeToken(request);

        // then
        verify(redisTemplate).delete("auth_code:" + authCode);
    }

    @Test
    void exchangeToken_Redis_조회시_NumberFormatException_처리() {
        // given
        String authCode = "test-auth-code";
        String invalidUserId = "invalid-user-id";

        when(valueOperations.get("auth_code:" + authCode)).thenReturn(invalidUserId);

        AuthTokenRequest request = new AuthTokenRequest(authCode);

        // when & then
        assertThrows(
                NumberFormatException.class,
                () -> authTokenService.exchangeToken(request)
        );

        verify(valueOperations).get("auth_code:" + authCode);
        verify(oauth2UserQuery, never()).findById(any());
        verify(jwtTokenProvider, never()).generateAccessToken(any());
        verify(jwtTokenProvider, never()).generateRefreshToken(any());
    }
}