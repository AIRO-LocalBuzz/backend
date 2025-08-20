package backend.airo.infra.promotion;

import backend.airo.infra.promotion.service.HtmlTemplateService;
import backend.airo.infra.promotion.service.ImageDownloadService;
import backend.airo.infra.promotion.service.PromotionImageService;
import backend.airo.infra.promotion.PlaywrightBrowserPool;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestPromotionConfiguration {

    @Bean
    @Primary
    public HtmlTemplateService testHtmlTemplateService() {
        return new HtmlTemplateService();
    }

    @Bean
    @Primary
    public ImageDownloadService testImageDownloadService() {
        WebClient mockWebClient = mock(WebClient.class);
        return new ImageDownloadService(mockWebClient);
    }

    @Bean
    @Primary
    public PlaywrightBrowserPool testBrowserPool() {
        return mock(PlaywrightBrowserPool.class);
    }

    @Bean
    @Primary
    public PromotionImageService testPromotionImageService() {
        return mock(PromotionImageService.class);
    }
}