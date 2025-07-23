package backend.airo.infra.config;

import org.springframework.context.annotation.Bean;

public class OpenApiFeignClientConfiguration {

    @Bean
    public OpenApiFeignClientConfiguration openApiRequestInterceptor() {
        return new OpenApiFeignClientConfiguration();
    }

}
