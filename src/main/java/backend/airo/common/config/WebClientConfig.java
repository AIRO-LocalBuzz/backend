package backend.airo.common.config;

import backend.airo.api.resolver.JwtTokenParsingArgumentResolver;
import backend.airo.api.resolver.UserPrincipalArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig implements WebMvcConfigurer {

    private final UserPrincipalArgumentResolver userPrincipalArgumentResolver;
    private final JwtTokenParsingArgumentResolver jwtTokenParsingArgumentResolver;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userPrincipalArgumentResolver);
        resolvers.add(jwtTokenParsingArgumentResolver);
    }
}