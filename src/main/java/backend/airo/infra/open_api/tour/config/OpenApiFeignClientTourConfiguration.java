package backend.airo.infra.open_api.tour.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class OpenApiFeignClientTourConfiguration {

    @Bean
    public OpenApiTourRequestInterceptor openApiTourRequestInterceptor() {
        return new OpenApiTourRequestInterceptor();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}