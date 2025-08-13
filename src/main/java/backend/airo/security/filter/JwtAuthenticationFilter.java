package backend.airo.security.filter;

import backend.airo.common.jwt.JwtAuthenticationToken;
import backend.airo.common.jwt.JwtTokenProvider;
import backend.airo.domain.auth.oauth2.query.FindOAuth2UserQuery;
import backend.airo.domain.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final FindOAuth2UserQuery findOAuth2UserQuery;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        log.info("=== JWT Filter 실행 시작 - URI: {} ===", requestURI);

        // Authorization 헤더에서 토큰 추출
        String token = extractTokenFromRequest(request);
        log.info("토큰 추출 결과: {}", token != null ? "성공 (길이: " + token.length() + ")" : "실패");

        if (token != null) {
            boolean isValid = jwtTokenProvider.validateToken(token);
            log.info("토큰 검증 결과: {}", isValid ? "유효" : "무효");

            if (isValid) {
                try {
                    // 토큰에서 사용자 ID 추출
                    Long userId = jwtTokenProvider.getUserIdFromToken(token);
                    log.info("JWT 토큰에서 사용자 ID 추출 성공 - User ID: {}", userId);

                    // 사용자 정보 조회
                    User user = findOAuth2UserQuery.findById(userId);
                    log.info("사용자 정보 조회 성공 - User: {}", user.getEmail());

                    // Spring Security 인증 객체 생성
                    JwtAuthenticationToken authentication = new JwtAuthenticationToken(
                            user,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );

                    // SecurityContext에 인증 정보 설정
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("SecurityContext에 인증 정보 설정 완료 - User ID: {}", userId);

                } catch (Exception e) {
                    log.error("JWT 인증 처리 중 오류 발생", e);
                    SecurityContextHolder.clearContext();
                }
            }
        } else {
            log.warn("Authorization 헤더에서 토큰을 찾을 수 없음");
        }

        log.info("=== JWT Filter 실행 완료 - URI: {} ===", requestURI);
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean shouldSkip = path.startsWith("/auth/") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/api/v1/init/") ||
                path.startsWith("/v3/api-docs/") ||
                path.startsWith("/api/login/oauth2/") ||
                path.startsWith("/login/oauth2/");

        log.info("필터 실행 여부 체크 - URI: {}, 스킵: {}", path, shouldSkip);
        return shouldSkip;
    }
}