package backend.airo.infra.open_api.area_find.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

public class OpenApiAreaRequestInterceptor implements RequestInterceptor {

    @Value("${openapi.service.key}")
    private String serviceKey;

    @Override
    public void apply(RequestTemplate template) {
        template.header("Content-type", "application/json");
        template.query("serviceKey", serviceKey);
        template.query("resId", "dong");
        template.query("type", "json");
    }
}