package backend.airo.api.global.swagger.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(getApiInfo())
                .servers(getServers())
                .components(new Components()
                        .addSecuritySchemes("Bearer Auth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT 토큰을 입력하세요 (Bearer 접두사 제외)")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Auth"));
    }



    /**
     * API 기본 정보
     */
    private Info getApiInfo() {
        return new Info()
                .title("LocalBuzz API")
                .description(getApiDescription())
                .version("v1.0.0")
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"))
                .contact(new Contact()
                        .name("team AIRO")
                        .email("team@airo.com")
                        .url("https://github.com/AIRO-LocalBuzz/backend"));
    }

    /**
     * API 설명
     */
    private String getApiDescription() {
        return """
                "지역 관광객 후기를 AI로 요약하여 소상공인 홍보자료로 전환하는 플랫폼 API"
                
                ### 주요 기능
                - 
                -
                -

                """;
    }

    /**
     * 서버 정보
     */
    private List<Server> getServers() {
        return List.of(
                new Server()
                        .url("http://localhost:9001/api")
                        .description("로컬 개발 서버"),
                new Server()
                        .url("https://localbuzz/api")
                        .description("운영 서버")
        );
    }

    /**
     * API 태그 분류
     */
    private List<Tag> getTags() {
        return List.of(
                new Tag()
                        .name("Test")
                        .description("테스트 관련 API"),
                new Tag()
                        .name("User")
                        .description("회원 관련 API"),
                new Tag()
                        .name("Review")
                        .description("리뷰 관련 API")
        );
    }
}