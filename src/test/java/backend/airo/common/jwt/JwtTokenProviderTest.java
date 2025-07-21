package backend.airo.common.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        // Secret must be at least 256 bits (32 bytes) for HS512
        String secret = "380d3be99d2d18d234723df05e0e2e077eab944d10cea7a804e4f708742ed7ec";
        jwtTokenProvider = new JwtTokenProvider(secret, 86400000L);
    }

    @Test
    void shouldGenerateToken() {
        // Given
        Long userId = 1L;
        String email = "test@example.com";

        // When
        String token = jwtTokenProvider.generateToken(userId, email);

        // Then
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void shouldValidateToken() {
        // Given
        Long userId = 1L;
        String email = "test@example.com";
        String token = jwtTokenProvider.generateToken(userId, email);

        // When
        boolean isValid = jwtTokenProvider.validateToken(token);

        // Then
        assertTrue(isValid);
    }

    @Test
    void shouldExtractUserIdFromToken() {
        // Given
        Long userId = 1L;
        String email = "test@example.com";
        String token = jwtTokenProvider.generateToken(userId, email);

        // When
        Long extractedUserId = jwtTokenProvider.getUserIdFromToken(token);

        // Then
        assertEquals(userId, extractedUserId);
    }

    @Test
    void shouldExtractEmailFromToken() {
        // Given
        Long userId = 1L;
        String email = "test@example.com";
        String token = jwtTokenProvider.generateToken(userId, email);

        // When
        String extractedEmail = jwtTokenProvider.getEmailFromToken(token);

        // Then
        assertEquals(email, extractedEmail);
    }

    @Test
    void shouldRejectInvalidToken() {
        // Given
        String invalidToken = "invalid.token.here";

        // When
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        // Then
        assertFalse(isValid);
    }
}