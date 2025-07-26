package backend.airo.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    private final String secretKey = "dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdG9rZW4tZ2VuZXJhdGlvbi1tdXN0LWJlLWF0LWxlYXN0LTI1Ni1iaXRz";
    private final long accessTokenValidityInSeconds = 3600L; // 1시간
    private final long refreshTokenValidityInSeconds = 86400L; // 24시간

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();

        // @Value 어노테이션으로 주입되는 값들을 직접 설정
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenValidityInSeconds", accessTokenValidityInSeconds);
        ReflectionTestUtils.setField(jwtTokenProvider, "refreshTokenValidityInSeconds", refreshTokenValidityInSeconds);
    }

    @Test
    void generateAccessToken_정상_토큰_생성() {
        // given
        Long userId = 1L;

        // when
        String token = jwtTokenProvider.generateAccessToken(userId);

        // then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals(userId, jwtTokenProvider.getUserIdFromToken(token));
    }

    @Test
    void generateRefreshToken_정상_토큰_생성() {
        // given
        Long userId = 1L;

        // when
        String token = jwtTokenProvider.generateRefreshToken(userId);

        // then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals(userId, jwtTokenProvider.getUserIdFromToken(token));
    }

    @Test
    void validateToken_유효한_토큰_true_반환() {
        // given
        Long userId = 1L;
        String token = jwtTokenProvider.generateAccessToken(userId);

        // when
        boolean isValid = jwtTokenProvider.validateToken(token);

        // then
        assertTrue(isValid);
    }

    @Test
    void validateToken_null_토큰_false_반환() {
        // when
        boolean isValid = jwtTokenProvider.validateToken(null);

        // then
        assertFalse(isValid);
    }

    @Test
    void validateToken_빈_토큰_false_반환() {
        // when
        boolean isValid = jwtTokenProvider.validateToken("");

        // then
        assertFalse(isValid);
    }

    @Test
    void validateToken_잘못된_형식_토큰_false_반환() {
        // given
        String invalidToken = "invalid.token.format";

        // when
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        // then
        assertFalse(isValid);
    }


    @Test
    void getUserIdFromToken_정상_사용자_ID_반환() {
        // given
        Long expectedUserId = 123L;
        String token = jwtTokenProvider.generateAccessToken(expectedUserId);

        // when
        Long actualUserId = jwtTokenProvider.getUserIdFromToken(token);

        // then
        assertEquals(expectedUserId, actualUserId);
    }

    @Test
    void getUserIdFromToken_잘못된_토큰_예외_발생() {
        // given
        String invalidToken = "invalid.token";

        // when & then
        assertThrows(Exception.class, () -> {
            jwtTokenProvider.getUserIdFromToken(invalidToken);
        });
    }

    @Test
    void getEmailFromToken_정상_이메일_반환() {
        // given - 이메일이 포함된 토큰을 생성하려면 JwtTokenProvider에 이메일을 추가하는 메서드가 필요
        // 현재는 userId만 포함되므로 이 테스트는 실제 구현에 따라 조정 필요
        Long userId = 1L;
        String token = jwtTokenProvider.generateAccessToken(userId);

        // when & then
        // 현재 JwtTokenProvider는 이메일을 토큰에 포함하지 않으므로 null이 예상됨
        String email = jwtTokenProvider.getEmailFromToken(token);
        assertNull(email);
    }

    @Test
    void getAccessTokenValidityInSeconds_정상_값_반환() {
        // when
        long validity = jwtTokenProvider.getAccessTokenValidityInSeconds();

        // then
        assertEquals(accessTokenValidityInSeconds, validity);
    }

    @Test
    void 다른_사용자_ID로_생성된_토큰들_구분됨() {
        // given
        Long userId1 = 1L;
        Long userId2 = 2L;

        // when
        String token1 = jwtTokenProvider.generateAccessToken(userId1);
        String token2 = jwtTokenProvider.generateAccessToken(userId2);

        // then
        assertNotEquals(token1, token2);
        assertEquals(userId1, jwtTokenProvider.getUserIdFromToken(token1));
        assertEquals(userId2, jwtTokenProvider.getUserIdFromToken(token2));
    }

    @Test
    void validateToken_변조된_토큰_false_반환() {
        // given
        Long userId = 1L;
        String validToken = jwtTokenProvider.generateAccessToken(userId);
        // 토큰 끝부분 몇 글자를 다른 글자로 변경
        String tamperedToken = validToken.substring(0, validToken.length() - 5) + "12345";

        // when
        boolean isValid = jwtTokenProvider.validateToken(tamperedToken);

        // then
        assertFalse(isValid);
    }

    @Test
    void 같은_사용자_ID로_생성된_토큰들도_다름() throws InterruptedException {
        // given
        Long userId = 1L;

        // when
        String token1 = jwtTokenProvider.generateAccessToken(userId);
        Thread.sleep(1001); // 1초 대기하여 iat 값이 달라지도록
        String token2 = jwtTokenProvider.generateAccessToken(userId);

        // then
        // 생성 시간이 다르므로 토큰이 달라야 함
        assertNotEquals(token1, token2);
        assertEquals(userId, jwtTokenProvider.getUserIdFromToken(token1));
        assertEquals(userId, jwtTokenProvider.getUserIdFromToken(token2));
    }

    @Test
    void 토큰_만료시간_검증() throws InterruptedException {
        // given
        Long userId = 1L;

        // 매우 짧은 만료시간으로 설정하여 테스트
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenValidityInSeconds", 1L);

        String token = jwtTokenProvider.generateAccessToken(userId);

        // when - 1초 대기
        Thread.sleep(1100);

        // then - 만료된 토큰은 유효하지 않아야 함
        assertFalse(jwtTokenProvider.validateToken(token));
    }

    @Test
    void secretKey_base64_디코딩_테스트() {
        // given
        Long userId = 1L;

        // when
        String token = jwtTokenProvider.generateAccessToken(userId);

        // then
        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals(userId, jwtTokenProvider.getUserIdFromToken(token));
    }
}