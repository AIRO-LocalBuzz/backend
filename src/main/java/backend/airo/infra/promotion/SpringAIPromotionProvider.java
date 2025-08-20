package backend.airo.infra.promotion;

import backend.airo.domain.promotion.PromotionRequest;
import backend.airo.domain.promotion.PromotionResult;
import backend.airo.domain.promotion.PromotionImageResult;
import backend.airo.domain.promotion.LLMProvider;
import backend.airo.domain.promotion.LLMProviderWithImage;
import backend.airo.infra.promotion.service.PromotionImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class SpringAIPromotionProvider implements LLMProviderWithImage {

//    @Qualifier("openAiChatClient")
    private final ChatClient openAiClient;
    private final ObjectMapper objectMapper;
    private final PromotionImageService promotionImageService;
    
    // 진행 상태 추적을 위한 임시 저장소
    private final ConcurrentHashMap<String, PromotionImageResult> taskStatusMap = new ConcurrentHashMap<>();

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
        return createFallbackPromotion(request);
    }

    @Override
    public PromotionImageResult generatePromotionWithImage(PromotionRequest request) {
        String taskId = UUID.randomUUID().toString();

        try {
            log.info("홍보물 생성 시작 (이미지 포함): taskId={}, postId={}", taskId, request.postId());

            // Step 1: LLM으로 텍스트 콘텐츠 생성
            PromotionResult promotionResult = generatePromotion(request);

            // Step 2: 초기 상태 저장
            PromotionImageResult initialResult = PromotionImageResult.withTextOnly(promotionResult, taskId);
            taskStatusMap.put(taskId, initialResult);

            // Step 3: 비동기로 이미지 생성 시작 (postId 전달)
            CompletableFuture<byte[]> imageGeneration = generateImageAsync(promotionResult, taskId, request.postId());

            // Step 4: 최종 결과 반환
            PromotionImageResult finalResult = PromotionImageResult.withImage(
                    promotionResult,
                    imageGeneration,
                    taskId
            );
            taskStatusMap.put(taskId, finalResult);

            return finalResult;

        } catch (Exception e) {
            log.error("홍보물 생성 실패: taskId={}", taskId, e);
            PromotionImageResult errorResult = PromotionImageResult.withError(
                    createFallbackPromotion(request),
                    taskId,
                    e.getMessage()
            );
            taskStatusMap.put(taskId, errorResult);
            return errorResult;
        }
    }

    @Override
    public PromotionImageResult getPromotionStatus(String taskId) {
        return taskStatusMap.get(taskId);
    }

    /**
     * 비동기 이미지 생성
     */
    public CompletableFuture<byte[]> generateImageAsync(PromotionResult promotionResult, String taskId, Long postId) {
        try {
            log.info("이미지 생성 비동기 처리 시작: taskId={}, postId={}", taskId, postId);

            return promotionImageService.generatePromotionImage(promotionResult, postId)  // postId 전달
                    .whenComplete((result, throwable) -> {
                        if (throwable != null) {
                            log.error("이미지 생성 실패: taskId={}, postId={}", taskId, postId, throwable);
                            updateTaskStatus(taskId, throwable.getMessage());
                        } else {
                            log.info("이미지 생성 완료: taskId={}, postId={}, size={} bytes", taskId, postId, result.length);
                        }
                    });

        } catch (Exception e) {
            log.error("이미지 생성 시작 실패: taskId={}, postId={}", taskId, postId, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * 태스크 상태 업데이트
     */
    private void updateTaskStatus(String taskId, String errorMessage) {
        PromotionImageResult currentStatus = taskStatusMap.get(taskId);
        if (currentStatus != null) {
            PromotionImageResult updatedStatus = PromotionImageResult.withError(
                    currentStatus.promotionResult(),
                    taskId,
                    errorMessage
            );
            taskStatusMap.put(taskId, updatedStatus);
        }
    }

    private PromotionResult parseResponse(String response, PromotionRequest request) {
        try {
            String jsonPart = extractJsonFromResponse(response);
            log.info("홍보물 생성 성공: {}", jsonPart);
            return objectMapper.readValue(jsonPart, PromotionResult.class);
        } catch (Exception e) {
            log.warn("응답 파싱 실패, 기본값 반환: {}", response, e);
            return new PromotionResult(
                    null, 
                    request.imageUrls().isEmpty() ? null : request.imageUrls().get(0), 
                    List.of(), 
                    request.tags(), 
                    request.title(), 
                    request.content()
            );
        }
    }

    private String extractJsonFromResponse(String response) {
        int start = response.indexOf("{");
        int end = response.lastIndexOf("}");

        if (start != -1 && end != -1 && start < end) {
            return response.substring(start, end + 1);
        }
        return response;
    }

    private String buildPrompt(PromotionRequest request) {
        return """
            당신은 감성 콘텐츠 마케터이자 지역 홍보 전문가입니다.
            아래 사용자 후기를 게시물 정보를 기반으로, [지역 소상공인을 위한 홍보물 콘텐츠]를 만들어주세요.
            게시물 정보:
            - 제목: %s
            - 내용: %s
            - 감정 태그: %s
            - 카테고리: %s
            - 위치: %s
            - 이미지 URL들: %s
            
            위 정보를 바탕으로 다음을 JSON 형식으로 생성해주세요 (반드시 JSON만 반환하세요):
            {
              "spotName": "가장 적합한 장소 이름",
              "mainImageUrl": "가장 적합한 대표 이미지 URL",
              "recommendedTags": ["내용기반 키워드 추출 1", "내용기반 키워드 추출 2", "내용기반 키워드 추출 3"],
              "emotions": ["추가 할 만한 감정1", "추가 할 만한 감정2"],
              "suggestedTitle": "감성 홍보 문구: 1줄 문장으로, 가게/공간의 매력을 담은 감성적인 문장",
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

    private PromotionResult createFallbackPromotion(PromotionRequest request) {
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