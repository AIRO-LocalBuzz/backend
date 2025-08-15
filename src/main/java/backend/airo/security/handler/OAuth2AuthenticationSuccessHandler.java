package backend.airo.security.handler;

import backend.airo.application.auth.oauth2.usecase.OAuth2AuthenticationUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2AuthenticationUseCase oauth2AuthenticationUseCase;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("OAuth2 로그인 성공 핸들러 시작");

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authToken.getAuthorizedClientRegistrationId(),
                authToken.getName());

        String accessToken = client.getAccessToken().getTokenValue();
        log.info("액세스 토큰: {}", accessToken);
        log.info("토큰 만료시간: {}", client.getAccessToken().getExpiresAt());

        try {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            log.info("OAuth2User 정보: {}", oauth2User.getAttributes());

            // 클라이언트 환경 확인
            boolean isLocalClient = isLocalClient(request);
            log.info("로컬 클라이언트 여부: {}", isLocalClient);

            // UseCase로 인증 처리 위임 (환경 정보 전달)
            String redirectUrl = oauth2AuthenticationUseCase.handleAuthenticationSuccess(oauth2User, accessToken, isLocalClient);

            log.info("리다이렉트 URL: {}", redirectUrl);
            response.sendRedirect(redirectUrl);

            log.info("OAuth2 로그인 성공 핸들러 완료");

        } catch (Exception e) {
            log.error("OAuth2 인증 성공 처리 중 오류 발생", e);
            response.sendRedirect("/auth/failure");
        }
    }

    private boolean isLocalClient(HttpServletRequest request) {
        // Referer 헤더로 클라이언트 확인
        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains("localhost:5173")) {
            return true;
        }

        // Origin 헤더로 클라이언트 확인
        String origin = request.getHeader("Origin");
        if (origin != null && origin.contains("localhost:5173")) {
            return true;
        }

        // Host 헤더로 확인 (프록시 환경 고려)
        String host = request.getHeader("Host");
        if (host != null && host.contains("localhost:5173")) {
            return true;
        }

        return false;
    }
}