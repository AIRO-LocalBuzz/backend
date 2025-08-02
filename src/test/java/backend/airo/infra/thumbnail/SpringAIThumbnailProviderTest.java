package backend.airo.infra.thumbnail;

import backend.airo.domain.thumbnail.ThumbnailRequest;
import backend.airo.domain.thumbnail.ThumbnailResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.ChatClient;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpringAIThumbnailProviderTest {

    @Mock
    private ChatClient openAiClient;

    @Mock
    private ChatClient ollamaClient;

    private ObjectMapper objectMapper;
    private SpringAIThumbnailProvider provider;

    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        provider = createProviderWithMocks();
    }

    private SpringAIThumbnailProvider createProviderWithMocks() throws Exception {
        SpringAIThumbnailProvider provider = new SpringAIThumbnailProvider(null, null);

        Field openAiField = SpringAIThumbnailProvider.class.getDeclaredField("openAiClient");
        openAiField.setAccessible(true);
        openAiField.set(provider, openAiClient);

//        Field ollamaField = SpringAIThumbnailProvider.class.getDeclaredField("ollamaClient");
//        ollamaField.setAccessible(true);
//        ollamaField.set(provider, ollamaClient);

        Field mapperField = SpringAIThumbnailProvider.class.getDeclaredField("objectMapper");
        mapperField.setAccessible(true);
        mapperField.set(provider, objectMapper);

        return provider;
    }

    @Test
    @DisplayName("OpenAI 성공 시 결과를 반환한다")
    void shouldReturnResultWhenOpenAISucceeds() {
        // given
        ThumbnailRequest request = createTestRequest();
        String jsonResponse = """
            {
              "spotName": "테스트 장소",
              "mainImageUrl": "image1.jpg",
              "recommendedTags": ["태그1", "태그2"],
              "emotions": ["행복", "기쁨"],
              "suggestedTitle": "개선된 제목"
            }
            """;

        when(openAiClient.call(anyString())).thenReturn(jsonResponse);

        // when
        ThumbnailResult result = provider.generateThumbnail(request);

        // then
        assertThat(result.spotName()).isEqualTo("테스트 장소");
        assertThat(result.mainImageUrl()).isEqualTo("image1.jpg");
        assertThat(result.recommendedTags()).containsExactly("태그1", "태그2");
        assertThat(result.emotions()).containsExactly("행복", "기쁨");
        assertThat(result.suggestedTitle()).isEqualTo("개선된 제목");
    }
//
//    @Test
//    @DisplayName("OpenAI 실패 시 Ollama로 폴백한다")
//    void shouldFallbackToOllamaWhenOpenAIFails() {
//        // given
//        ThumbnailRequest request = createTestRequest();
//        String jsonResponse = """
//            {
//              "spotName": "폴백 장소",
//              "mainImageUrl": "image2.jpg",
//              "recommendedTags": ["폴백태그"],
//              "suggestedTitle": "폴백 제목",
//              "suggestedTitle": "폴백 제목"
//            }
//            """;
//
//        when(openAiClient.call(anyString())).thenThrow(new RuntimeException("OpenAI 오류"));
//        when(ollamaClient.call(anyString())).thenReturn(jsonResponse);
//
//        // when
//        ThumbnailResult result = provider.generateThumbnail(request);
//
//        // then
//        assertThat(result.spotName()).isEqualTo("폴백 장소");
//        assertThat(result.mainImageUrl()).isEqualTo("image2.jpg");
//        assertThat(result.suggestedTitle()).isEqualTo("폴백 제목");
//    }

    @Test
    @DisplayName("모든 LLM 실패 시 기본값을 반환한다")
    void shouldReturnFallbackWhenAllLLMsFail() {
        // given
        ThumbnailRequest request = createTestRequest();

        when(openAiClient.call(anyString())).thenThrow(new RuntimeException("OpenAI 오류"));
//        when(ollamaClient.call(anyString())).thenThrow(new RuntimeException("Ollama 오류"));

        // when
        ThumbnailResult result = provider.generateThumbnail(request);

        // then
        assertThat(result.mainImageUrl()).isEqualTo("image1.jpg");
        assertThat(result.suggestedTitle()).isEqualTo("테스트 제목");
    }

    private ThumbnailRequest createTestRequest() {
        return new ThumbnailRequest(
                "테스트 내용",
                "테스트 제목",
                List.of("태그1", "태그2"),
                "CAFE",
                "서울",
                List.of("image1.jpg", "image2.jpg")
        );
    }

//    // 테스트용 헬퍼 클래스
//    private static class SpringAIThumbnailProviderTestHelper extends SpringAIThumbnailProvider {
//        public SpringAIThumbnailProviderTestHelper(ChatClient openAi, ChatClient ollama, ObjectMapper mapper) {
//            super();
//            // 리플렉션으로 필드 설정하거나 패키지 프라이빗 생성자 추가 필요
//        }
//    }
}