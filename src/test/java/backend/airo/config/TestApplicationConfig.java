package backend.airo.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestApplicationConfig {

    /**
     * 테스트용 환경변수 값들을 Bean으로 등록
     */
    @Bean
    @Primary
    public String openaiApiKey() {
        return "test-dummy-key";
    }

    @Bean
    @Primary
    public String dbUrl() {
        return "jdbc:h2:mem:testdb";
    }

    @Bean
    @Primary
    public String dbUsername() {
        return "sa";
    }

    @Bean
    @Primary
    public String dbPassword() {
        return "";
    }
}