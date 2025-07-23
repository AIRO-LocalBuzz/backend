package backend.airo.application.auth.oauth2;

import backend.airo.persistence.user.entity.ProviderType;
import backend.airo.persistence.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @Mock
    private OAuth2UserProcessingService oauth2UserProcessingService;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        mockUser = UserEntity.createOAuth2User(
                "test@example.com",
                "Test User",
                ProviderType.GOOGLE,
                "google123"
        );
    }

    @Test
    void getProviderType_Google_반환() {
        // when
        ProviderType result = customOAuth2UserService.getProviderType("google");

        // then
        assertEquals(ProviderType.GOOGLE, result);
    }

    @Test
    void getProviderType_Kakao_반환() {
        // when
        ProviderType result = customOAuth2UserService.getProviderType("kakao");

        // then
        assertEquals(ProviderType.KAKAO, result);
    }

    @Test
    void getProviderType_대소문자_구분_없음() {
        // when & then
        assertEquals(ProviderType.GOOGLE, customOAuth2UserService.getProviderType("GOOGLE"));
        assertEquals(ProviderType.KAKAO, customOAuth2UserService.getProviderType("KAKAO"));
    }

    @Test
    void getProviderType_지원하지_않는_제공자_예외_발생() {
        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> customOAuth2UserService.getProviderType("github")
        );

        assertEquals("지원하지 않는 OAuth2 제공자입니다: github", exception.getMessage());
    }

//    @Test
//    void loadUser_정상_처리_과정_검증() {
//        // given
//        OAuth2UserRequest userRequest = createOAuth2UserRequest("google");
//        Map<String, Object> attributes = Map.of(
//                "sub", "google123",
//                "email", "test@example.com",
//                "name", "Test User"
//        );
//
//        // OAuth2UserProcessingService 모킹
//        when(oauth2UserProcessingService.processOAuth2User(ProviderType.GOOGLE, attributes))
//                .thenReturn(mockUser);
//
//        // CustomOAuth2UserService를 부분 모킹 (callSuperLoadUser만)
//        CustomOAuth2UserService spyService = spy(customOAuth2UserService);
//        OAuth2User mockOAuth2User = mock(OAuth2User.class);
//        when(mockOAuth2User.getAttributes()).thenReturn(attributes);
//
//        // super.loadUser 대신 callSuperLoadUser 모킹
//        doReturn(mockOAuth2User).when(spyService).callSuperLoadUser(any());
//
//        // when
//        OAuth2User result = spyService.loadUser(userRequest);
//
//        // then
//        assertNotNull(result);
//        assertTrue(result instanceof CustomOAuth2User);
//        CustomOAuth2User customUser = (CustomOAuth2User) result;
//        assertEquals(mockUser, customUser.getUser());
//        assertEquals(attributes, customUser.getAttributes());
//
//        // 의존성 호출 검증
//        verify(spyService).callSuperLoadUser(userRequest);
//        verify(oauth2UserProcessingService).processOAuth2User(ProviderType.GOOGLE, attributes);
//    }
//
//    @Test
//    void loadUser_OAuth2UserProcessingService_예외시_OAuth2AuthenticationException_발생() {
//        // given
//        OAuth2UserRequest userRequest = createOAuth2UserRequest("google");
//        Map<String, Object> attributes = Map.of("sub", "google123");
//
//        when(oauth2UserProcessingService.processOAuth2User(any(), any()))
//                .thenThrow(new RuntimeException("Processing failed"));
//
//        CustomOAuth2UserService spyService = spy(customOAuth2UserService);
//        OAuth2User mockOAuth2User = mock(OAuth2User.class);
//        when(mockOAuth2User.getAttributes()).thenReturn(attributes);
//
//        doReturn(mockOAuth2User).when(spyService).callSuperLoadUser(any());
//
//        // when & then
//        OAuth2AuthenticationException exception = assertThrows(
//                OAuth2AuthenticationException.class,
//                () -> spyService.loadUser(userRequest)
//        );
//
//        assertEquals("OAuth2 processing failed", exception.getMessage());
//        verify(oauth2UserProcessingService).processOAuth2User(ProviderType.GOOGLE, attributes);
//    }
//
//    @Test
//    void loadUser_지원하지_않는_제공자_예외_발생() {
//        // given
//        OAuth2UserRequest userRequest = createOAuth2UserRequest("github");
//        Map<String, Object> attributes = Map.of("id", "github123");
//
//        CustomOAuth2UserService spyService = spy(customOAuth2UserService);
//        OAuth2User mockOAuth2User = mock(OAuth2User.class);
//        when(mockOAuth2User.getAttributes()).thenReturn(attributes);
//
//        doReturn(mockOAuth2User).when(spyService).callSuperLoadUser(any());
//
//        // when & then
//        OAuth2AuthenticationException exception = assertThrows(
//                OAuth2AuthenticationException.class,
//                () -> spyService.loadUser(userRequest)
//        );
//
//        assertEquals("OAuth2 processing failed", exception.getMessage());
//        verify(oauth2UserProcessingService, never()).processOAuth2User(any(), any());
//    }
//
//    // Helper method
//    private OAuth2UserRequest createOAuth2UserRequest(String registrationId) {
//        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId(registrationId)
//                .clientId("test-client-id")
//                .clientSecret("test-client-secret")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUri("http://localhost:8080/login/oauth2/code/" + registrationId)
//                .scope("profile", "email")
//                .authorizationUri("https://example.com/oauth/authorize")
//                .tokenUri("https://example.com/oauth/token")
//                .userInfoUri("https://example.com/userinfo")
//                .userNameAttributeName("sub")
//                .build();
//
//        OAuth2AccessToken accessToken = mock(OAuth2AccessToken.class);
//        when(accessToken.getTokenValue()).thenReturn("test-access-token");
//
//        OAuth2UserRequest userRequest = mock(OAuth2UserRequest.class);
//        when(userRequest.getClientRegistration()).thenReturn(clientRegistration);
//        when(userRequest.getAccessToken()).thenReturn(accessToken);
//
//        return userRequest;
//    }
}