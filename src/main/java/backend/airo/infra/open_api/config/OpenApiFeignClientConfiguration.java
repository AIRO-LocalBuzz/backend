package backend.airo.infra.open_api.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class OpenApiFeignClientConfiguration {

    @Bean
    public OpenApiRequestInterceptor openApiRequestInterceptor() {
        return new OpenApiRequestInterceptor();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}