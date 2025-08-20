package backend.airo.infra.promotion.config;

import backend.airo.domain.promotion.LLMProvider;
import backend.airo.domain.promotion.LLMProviderWithImage;
import backend.airo.infra.promotion.SpringAIPromotionProvider;

import backend.airo.infra.promotion.service.PromotionImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AIConfig {

    @Bean
    public ChatClient primaryChatClient(@Qualifier("openAiChatClient") ChatClient openAiClient) {
        return openAiClient;
    }

    @Primary
    @Bean("promotionProviderWithImage")
    public LLMProviderWithImage promotionProviderWithImage(
            @Qualifier("openAiChatClient") ChatClient openAiClient,
            ObjectMapper objectMapper,
            PromotionImageService promotionImageService) {

        return new SpringAIPromotionProvider(openAiClient, objectMapper, promotionImageService);
    }

    @Bean("thumbnailLLMProvider")
    public LLMProvider legacyLLMProvider(
            @Qualifier("openAiChatClient") ChatClient openAiClient,
            ObjectMapper objectMapper,
            PromotionImageService promotionImageService) {

        return new SpringAIPromotionProvider(openAiClient, objectMapper, promotionImageService);
    }
}