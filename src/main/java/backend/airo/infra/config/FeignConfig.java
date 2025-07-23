package backend.airo.infra.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(
        basePackages = "backend.airo.infra.open_api.client"
)
public class FeignConfig {
}
