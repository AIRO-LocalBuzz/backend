package backend.airo.infra.thumbnail.config;

import backend.airo.domain.thumbnail.LLMProvider;
import backend.airo.infra.thumbnail.SpringAIPromotionProvider;
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
    @Bean("thumbnailLLMProvider")
    public LLMProvider multiLLMProvider(
            @Qualifier("openAiChatClient") ChatClient openAiClient,
            ObjectMapper objectMapper) {

        return new SpringAIPromotionProvider(openAiClient, objectMapper);
    }
}