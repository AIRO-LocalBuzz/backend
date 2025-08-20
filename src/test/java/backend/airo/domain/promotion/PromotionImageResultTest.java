package backend.airo.domain.promotion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PromotionImageResult 도메인 테스트")
class PromotionImageResultTest {

    @Test
    @DisplayName("텍스트만 있는 초기 상태로 객체를 생성할 수 있다")
    void shouldCreateWithTextOnly() {
        // given
        PromotionResult promotionResult = createTestPromotionResult();
        String taskId = "test-task-id";

        // when
        PromotionImageResult result = PromotionImageResult.withTextOnly(promotionResult, taskId);

        // then
        assertThat(result.promotionResult()).isEqualTo(promotionResult);
        assertThat(result.taskId()).isEqualTo(taskId);
        assertThat(result.status()).isEqualTo(PromotionImageStatus.PROCESSING);
        assertThat(result.imageData()).isNull();
        assertThat(result.errorMessage()).isNull();
        assertThat(result.createdAt()).isBefore(LocalDateTime.now().plusSeconds(1));
    }

    @Test
    @DisplayName("이미지가 포함된 완료 상태로 객체를 생성할 수 있다")
    void shouldCreateWithImage() {
        // given
        PromotionResult promotionResult = createTestPromotionResult();
        String taskId = "test-task-id";
        CompletableFuture<byte[]> imageData = CompletableFuture.completedFuture("image-data".getBytes());

        // when
        PromotionImageResult result = PromotionImageResult.withImage(promotionResult, imageData, taskId);

        // then
        assertThat(result.promotionResult()).isEqualTo(promotionResult);
        assertThat(result.taskId()).isEqualTo(taskId);
        assertThat(result.status()).isEqualTo(PromotionImageStatus.COMPLETED);
        assertThat(result.imageData()).isEqualTo(imageData);
        assertThat(result.errorMessage()).isNull();
    }

    @Test
    @DisplayName("에러 상태로 객체를 생성할 수 있다")
    void shouldCreateWithError() {
        // given
        PromotionResult promotionResult = createTestPromotionResult();
        String taskId = "test-task-id";
        String errorMessage = "이미지 생성 실패";

        // when
        PromotionImageResult result = PromotionImageResult.withError(promotionResult, taskId, errorMessage);

        // then
        assertThat(result.promotionResult()).isEqualTo(promotionResult);
        assertThat(result.taskId()).isEqualTo(taskId);
        assertThat(result.status()).isEqualTo(PromotionImageStatus.FAILED);
        assertThat(result.imageData()).isNull();
        assertThat(result.errorMessage()).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("이미지 준비 완료 상태를 정확히 확인할 수 있다")
    void shouldCorrectlyCheckImageReady() {
        // given
        PromotionResult promotionResult = createTestPromotionResult();
        String taskId = "test-task-id";

        // Case 1: 완료된 이미지 데이터
        CompletableFuture<byte[]> completedImageData = CompletableFuture.completedFuture("image-data".getBytes());
        PromotionImageResult completedResult = PromotionImageResult.withImage(promotionResult, completedImageData, taskId);

        // Case 2: 진행 중인 이미지 데이터
        CompletableFuture<byte[]> pendingImageData = new CompletableFuture<>();
        PromotionImageResult pendingResult = PromotionImageResult.withImage(promotionResult, pendingImageData, taskId);

        // Case 3: 실패한 이미지 데이터
        CompletableFuture<byte[]> failedImageData = CompletableFuture.failedFuture(new RuntimeException("실패"));
        PromotionImageResult failedResult = PromotionImageResult.withImage(promotionResult, failedImageData, taskId);

        // Case 4: 이미지 데이터가 없는 경우
        PromotionImageResult noImageResult = PromotionImageResult.withTextOnly(promotionResult, taskId);

        // then
        assertThat(completedResult.isImageReady()).isTrue();
        assertThat(pendingResult.isImageReady()).isFalse();
        assertThat(failedResult.isImageReady()).isFalse();
        assertThat(noImageResult.isImageReady()).isFalse();
    }

    private PromotionResult createTestPromotionResult() {
        return new PromotionResult(
                "테스트 카페",
                "https://example.com/image.jpg",
                List.of("커피", "분위기", "맛집"),
                List.of("행복", "만족"),
                "감성 가득한 동네 카페에서의 특별한 시간",
                "직접 로스팅한 원두로 내린 커피가 일품입니다."
        );
    }
}