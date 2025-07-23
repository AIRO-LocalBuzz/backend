package backend.airo.security.config;

import backend.airo.application.auth.oauth2.CustomOAuth2UserService;
import backend.airo.domain.auth.oauth2.query.OAuth2UserQuery;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.data.redis.core.RedisTemplate;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2UserQuery oauth2UserQuery;
    private final RedisTemplate<String, String> redisTemplate;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(authRequests -> authRequests
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/api/auth/**",
                                "/api/oauth2/**",
                                "/api/login/oauth2/**",
                                "/api/v1/test/**",
                                "/api/v1/**",
                                "/api/actuator/health"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler((request, response, authentication) -> {
                            log.info("OAuth2 로그인 성공 핸들러 시작");

                            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                            log.info("OAuth2User 정보: {}", oauth2User.getAttributes());

                            String providerId = oauth2User.getAttribute("provider_id");
                            ProviderType providerType = oauth2User.getAttribute("provider_type");

                            log.info("Provider ID: {}, Provider Type: {}", providerId, providerType);

                            Optional<User> userOptional = oauth2UserQuery.findByProviderIdAndProviderType(providerId, providerType);

                            if (userOptional.isPresent()) {
                                User user = userOptional.get();
                                log.info("기존 사용자 찾음 - User ID: {}, Email: {}", user.getId(), user.getEmail());

                                // 임시 코드 생성 (1분 유효) - Redis에 저장
                                String tempCode = UUID.randomUUID().toString();
                                log.info("임시 인증 코드 생성: {}", tempCode);

                                redisTemplate.opsForValue().set(
                                        "auth_code:" + tempCode,
                                        user.getId().toString(),
                                        1, TimeUnit.MINUTES
                                );
                                log.info("Redis에 임시 코드 저장 완료");

                                String redirectUrl = "http://localhost:3000/auth/success?code=" + tempCode;
                                log.info("성공 리다이렉트 URL: {}", redirectUrl);
                                response.sendRedirect(redirectUrl);
                            } else {
                                log.warn("사용자를 찾을 수 없음 - Provider ID: {}, Provider Type: {}", providerId, providerType);
                                log.info("실패 리다이렉트 URL: http://localhost:3000/auth/failure");
                                response.sendRedirect("http://localhost:3000/auth/failure");
                            }

                            log.info("OAuth2 로그인 성공 핸들러 완료");
                        })
                );

        return http.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080", "http://localhost:9001"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
