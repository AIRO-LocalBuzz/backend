package backend.airo.security.handler;

import backend.airo.application.auth.oauth2.usecase.OAuth2AuthenticationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2AuthenticationUseCase oauth2AuthenticationUseCase;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("OAuth2 로그인 성공 핸들러 시작");

        try {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            log.info("OAuth2User 정보: {}", oauth2User.getAttributes());

            // UseCase로 인증 처리 위임
            String redirectUrl = oauth2AuthenticationUseCase.handleAuthenticationSuccess(oauth2User);

            log.info("리다이렉트 URL: {}", redirectUrl);
            response.sendRedirect(redirectUrl);

            log.info("OAuth2 로그인 성공 핸들러 완료");

        } catch (Exception e) {
            log.error("OAuth2 인증 성공 처리 중 오류 발생", e);
            response.sendRedirect("http://localhost:3000/auth/failure");
        }
    }
}