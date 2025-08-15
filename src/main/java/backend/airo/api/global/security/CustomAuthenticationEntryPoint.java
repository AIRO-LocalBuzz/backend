package backend.airo.api.global.security;

import backend.airo.api.global.dto.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import java.io.IOException;


public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String requestURI = request.getRequestURI();

        // OAuth2 authorization 요청은 그대로 통과
        if (requestURI.startsWith("/api/oauth2/authorization/")) {
            // OAuth2 authorization 요청을 Spring Security의 OAuth2 처리기로 전달
            response.sendRedirect(requestURI + "?" + request.getQueryString());
            return;
        }

        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String errorMessage = "인증 실패 또는 권한 없음 : 토큰 헤더가 필요합니다.";

        Response errorResponse = Response.error(HttpStatus.UNAUTHORIZED.toString(), errorMessage);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}