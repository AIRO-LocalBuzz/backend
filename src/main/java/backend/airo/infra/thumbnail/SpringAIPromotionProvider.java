package backend.airo.infra.thumbnail;

import backend.airo.domain.promotion.PromotionRequest;
import backend.airo.domain.thumbnail.LLMProvider;
import backend.airo.domain.thumbnail.ThumbnailRequest;
import backend.airo.domain.promotion.PromotionResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class SpringAIPromotionProvider implements LLMProvider {

    @Qualifier("openAiChatClient")
    private final ChatClient openAiClient;

//    @Qualifier("ollamaChatClient")
//    private final ChatClient ollamaClient;

    private final ObjectMapper objectMapper;

    @Override
    public PromotionResult generatePromotion(PromotionRequest request) {
        List<ChatClient> clients = List.of(openAiClient);

        for (ChatClient client : clients) {
            try {
                String response = client.call(buildPrompt(request));

                return parseResponse(response, request);
            } catch (Exception e) {
                log.warn("LLM 호출 실패, 다음 제공자 시도", e);
            }
        }
        return createFallbackThumbnail(request);
    }

    private PromotionResult parseResponse(String response, PromotionRequest request) {
        try {
            // JSON 부분만 추출 (LLM 응답에서 JSON 외 텍스트 제거)
            String jsonPart = extractJsonFromResponse(response);
            log.info("썸네일 생성 성공:" + jsonPart);
            return objectMapper.readValue(jsonPart, PromotionResult.class);
        } catch (Exception e) {
            log.warn("응답 파싱 실패, 기본값 반환: {}", response, e);
            return new PromotionResult(null, request.imageUrls().get(0), List.of(), request.tags(), request.title(), request.content());
        }
    }


    private String extractJsonFromResponse(String response) {
        int start = response.indexOf("{");
        int end = response.lastIndexOf("}");

        if (start != -1 && end != -1 && start < end) {
            return response.substring(start, end + 1);
        }

        return response; // JSON 형태가 아니면 원본 반환
    }

    private String buildPrompt(PromotionRequest request) {
        return """
            당신은 감성 콘텐츠 마케터이자 지역 홍보 전문가입니다.
            아래 사용자 후기를 게시물 정보를 기반으로, [지역 소상공인을 위한 썸네일 콘텐츠]를 만들어주세요.
            게시물 정보:
            - 제목: %s
            - 내용: %s
            - 감정 태그: %s
            - 카테고리: %s
            - 위치: %s
            - 이미지 URL들: %s
            
            위 정보를 바탕으로 다음을 JSON 형식으로 생성해주세요 (반드시 JSON만 반환하세요)":
            {
              "spotName": "가장 적합한 장소 이름",
              "mainImageUrl": "가장 적합한 대표 이미지 URL",
              "recommendedTags": ["내용기반 키워드 추출 1", "내용기반 키워드 추출 2", "내용기반 키워드 추출 3"],
              "emotions": ["추가 할 만한 감정1", "추가 할 만한 감정2"],
              "suggestedTitle": "감성 홍보 문구: 1줄 문장으로, 가게/공간의 매력을 담은 감성적인 문장"
                "content": "원본글에서 가게의 특징을 나타내는 내용을 3문장으로 요약"
            }
            """.formatted(
                request.title(),
                request.content(),
                String.join(", ", request.tags()),
                request.category(),
                request.location(),
                String.join(", ", request.imageUrls())
        );
    }

    private PromotionResult createFallbackThumbnail(PromotionRequest request) {
        return new PromotionResult(
                null,
                request.imageUrls().isEmpty() ? null : request.imageUrls().get(0),
                request.tags().size() >= 3 ?
                        request.tags().subList(0, 3) :
                        request.tags(),
                request.tags().size() >= 3 ?
                        request.tags().subList(0, 3) :
                        request.tags(),
                request.title(),
                request.content()
        );
    }
}