package backend.airo.api.auth;

import backend.airo.api.auth.dto.AuthResponse;
import backend.airo.api.auth.dto.SocialLoginRequest;
import backend.airo.application.auth.service.SocialLoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private SocialLoginService socialLoginService;

    @InjectMocks
    private AuthController authController;

    @Test
    void socialLoginSuccess() {
        // Given
        SocialLoginRequest request = new SocialLoginRequest("test-token", "google");
        AuthResponse expectedResponse = new AuthResponse("jwt-token", 
            new AuthResponse.UserInfo(1L, "test@example.com", "Test User", "google"));
        
        when(socialLoginService.socialLogin(request)).thenReturn(expectedResponse);

        // When
        ResponseEntity<AuthResponse> response = authController.socialLogin(request);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(socialLoginService).socialLogin(request);
    }

    @Test
    void socialLoginFailure() {
        // Given
        SocialLoginRequest request = new SocialLoginRequest("invalid-token", null);
        
        when(socialLoginService.socialLogin(request)).thenThrow(new RuntimeException("Invalid token"));

        // When
        ResponseEntity<AuthResponse> response = authController.socialLogin(request);

        // Then
        assertEquals(401, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void googleLoginSuccess() {
        // Given
        String token = "test-google-token";
        AuthResponse expectedResponse = new AuthResponse("jwt-token", 
            new AuthResponse.UserInfo(1L, "test@example.com", "Test User", "google"));
        
        when(socialLoginService.loginWithGoogle(token)).thenReturn(expectedResponse);

        // When
        ResponseEntity<AuthResponse> response = authController.googleLogin(token);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void kakaoLoginSuccess() {
        // Given
        String token = "test-kakao-token";
        AuthResponse expectedResponse = new AuthResponse("jwt-token", 
            new AuthResponse.UserInfo(1L, "test@example.com", "Test User", "kakao"));
        
        when(socialLoginService.loginWithKakao(token)).thenReturn(expectedResponse);

        // When
        ResponseEntity<AuthResponse> response = authController.kakaoLogin(token);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
    }
}