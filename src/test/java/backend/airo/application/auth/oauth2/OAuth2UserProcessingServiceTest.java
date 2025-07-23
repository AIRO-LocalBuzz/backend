package backend.airo.application.auth.oauth2;

import backend.airo.domain.auth.oauth2.OAuth2UserInfo;
import backend.airo.domain.auth.oauth2.command.OAuth2UserCommand;
import backend.airo.domain.auth.oauth2.query.OAuth2UserQuery;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuth2UserProcessingServiceTest {

    @Mock
    private OAuth2UserQuery oauth2UserQuery;

    @Mock
    private OAuth2UserCommand oauth2UserCommand;

    @InjectMocks
    private OAuth2UserProcessingService oauth2UserProcessingService;

    private Map<String, Object> googleAttributes;
    private User existingUser;
    private User newUser;

    @BeforeEach
    void setUp() {
        googleAttributes = Map.of(
                "sub", "google123",
                "email", "test@example.com",
                "name", "Test User"
        );

        existingUser = User.createOAuth2User(
                "test@example.com",
                "Old Name",
                ProviderType.GOOGLE,
                "google123"
        );

        newUser = User.createOAuth2User(
                "test@example.com",
                "Test User",
                ProviderType.GOOGLE,
                "google123"
        );
    }

    @Test
    void processOAuth2User_새로운_사용자_생성() {
        // given
        when(oauth2UserQuery.findByProviderIdAndProviderType("google123", ProviderType.GOOGLE))
                .thenReturn(Optional.empty());
        when(oauth2UserCommand.save(any(User.class))).thenReturn(newUser);

        // when
        User result = oauth2UserProcessingService.processOAuth2User(ProviderType.GOOGLE, googleAttributes);

        // then
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Test User", result.getNickname());
        assertEquals(ProviderType.GOOGLE, result.getProviderType());
        assertEquals("google123", result.getProviderId());

        verify(oauth2UserQuery).findByProviderIdAndProviderType("google123", ProviderType.GOOGLE);
        verify(oauth2UserCommand).save(any(User.class));
    }

    @Test
    void processOAuth2User_기존_사용자_업데이트() {
        // given
        when(oauth2UserQuery.findByProviderIdAndProviderType("google123", ProviderType.GOOGLE))
                .thenReturn(Optional.of(existingUser));
        when(oauth2UserCommand.save(existingUser)).thenReturn(existingUser);

        // when
        User result = oauth2UserProcessingService.processOAuth2User(ProviderType.GOOGLE, googleAttributes);

        // then
        assertNotNull(result);
        assertEquals(existingUser, result);

        verify(oauth2UserQuery).findByProviderIdAndProviderType("google123", ProviderType.GOOGLE);
        verify(oauth2UserCommand).save(existingUser);
    }

    @Test
    void processOAuth2User_이메일이_없는_경우_예외_발생() {
        // given
        Map<String, Object> attributesWithoutEmail = Map.of(
                "sub", "google123",
                "name", "Test User"
        );

        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> oauth2UserProcessingService.processOAuth2User(ProviderType.GOOGLE, attributesWithoutEmail)
        );

        assertEquals("Email not found from OAuth2 provider", exception.getMessage());
        verify(oauth2UserQuery, never()).findByProviderIdAndProviderType(anyString(), any(ProviderType.class));
        verify(oauth2UserCommand, never()).save(any(User.class));
    }

    @Test
    void processOAuth2User_이메일이_빈_문자열인_경우_예외_발생() {
        // given
        Map<String, Object> attributesWithEmptyEmail = Map.of(
                "sub", "google123",
                "email", "",
                "name", "Test User"
        );

        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> oauth2UserProcessingService.processOAuth2User(ProviderType.GOOGLE, attributesWithEmptyEmail)
        );

        assertEquals("Email not found from OAuth2 provider", exception.getMessage());
    }

    @Test
    void processOAuth2User_이메일이_null인_경우_예외_발생() {
        // given
        Map<String, Object> attributesWithNullEmail = new HashMap<>();
        attributesWithNullEmail.put("sub", "google123");
        attributesWithNullEmail.put("name", "Test User");
        attributesWithNullEmail.put("email", null);

        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> oauth2UserProcessingService.processOAuth2User(ProviderType.GOOGLE, attributesWithNullEmail)
        );

        assertEquals("Email not found from OAuth2 provider", exception.getMessage());
    }

//    @Test
//    void processOAuth2User_카카오_제공자_처리() {
//        // given
//        Map<String, Object> kakaoAttributes = Map.of(
//                "id", 123456789L,
//                "kakao_account", Map.of(
//                        "email", "kakao@example.com"
//                )
//        );
//
//        when(oauth2UserQuery.findByProviderIdAndProviderType("123456789", ProviderType.KAKAO))
//                .thenReturn(Optional.empty());
//        when(oauth2UserCommand.save(any(User.class))).thenReturn(newUser);
//
//        // when
//        User result = oauth2UserProcessingService.processOAuth2User(ProviderType.KAKAO, kakaoAttributes);
//
//        // then
//        assertNotNull(result);
//        verify(oauth2UserQuery).findByProviderIdAndProviderType("123456789", ProviderType.KAKAO);
//        verify(oauth2UserCommand).save(any(User.class));
//    }



    @Test
    void processOAuth2User_사용자_조회_실패시_새_사용자_생성() {
        // given
        when(oauth2UserQuery.findByProviderIdAndProviderType("google123", ProviderType.GOOGLE))
                .thenReturn(Optional.empty());
        when(oauth2UserCommand.save(any(User.class))).thenReturn(newUser);

        // when
        User result = oauth2UserProcessingService.processOAuth2User(ProviderType.GOOGLE, googleAttributes);

        // then
        assertNotNull(result);
        verify(oauth2UserCommand).save(any(User.class));
    }
}