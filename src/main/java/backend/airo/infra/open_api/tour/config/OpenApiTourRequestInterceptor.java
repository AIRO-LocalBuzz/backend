package backend.airo.infra.open_api.tour.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

public class OpenApiTourRequestInterceptor implements RequestInterceptor {

    @Value("${openapi.service.key}")
    private String serviceKey;

    @Override
    public void apply(RequestTemplate template) {
        template.header("Content-type", "application/json");
        template.query("serviceKey", serviceKey);
        template.query("_type", "json");
        template.query("MobileOS", "WEB");
        template.query("MobileApp", "AIRO");
        template.query("arrange", "D");
        template.query("numOfRows", "10000");
    }
}