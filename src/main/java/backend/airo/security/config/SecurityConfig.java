package backend.airo.security.config;

import backend.airo.application.auth.oauth2.CustomOAuth2UserService;
import backend.airo.domain.auth.oauth2.query.OAuth2UserQuery;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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
                            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                            String providerId = oauth2User.getAttribute("provider_id");
                            ProviderType providerType = oauth2User.getAttribute("provider_type");

                            Optional<User> userOptional = oauth2UserQuery.findByProviderIdAndProviderType(providerId, providerType);

                            if (userOptional.isPresent()) {
                                User user = userOptional.get();

                                // 임시 코드 생성 (1분 유효)
                                String tempCode = UUID.randomUUID().toString();
                                redisTemplate.opsForValue().set(
                                        "auth_code:" + tempCode,
                                        user.getId().toString(),
                                        1, TimeUnit.MINUTES
                                );

                                String redirectUrl = "http://localhost:3000/auth/success?code=" + tempCode;
                                response.sendRedirect(redirectUrl);
                            } else {
                                response.sendRedirect("http://localhost:3000/auth/failure");
                            }
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
