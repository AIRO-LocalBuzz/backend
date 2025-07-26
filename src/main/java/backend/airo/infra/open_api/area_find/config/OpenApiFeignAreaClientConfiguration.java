package backend.airo.infra.open_api.area_find.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class OpenApiFeignAreaClientConfiguration {

    @Bean
    public OpenApiAreaRequestInterceptor openApiRequestInterceptor() {
        return new OpenApiAreaRequestInterceptor();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}