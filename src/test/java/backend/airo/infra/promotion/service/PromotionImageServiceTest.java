package backend.airo.infra.promotion.service;

import backend.airo.domain.promotion.PromotionResult;
import backend.airo.infra.promotion.PlaywrightBrowserPool;
import backend.airo.infra.promotion.service.HtmlTemplateService;
import backend.airo.infra.promotion.service.ImageDownloadService;
import backend.airo.infra.promotion.service.PromotionImageService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PromotionImageService 테스트")
class PromotionImageServiceTest {

    @Mock
    private ImageDownloadService imageDownloadService;

    @Mock
    private HtmlTemplateService htmlTemplateService;

    @Mock
    private PlaywrightBrowserPool browserPool;

    private PromotionImageService promotionImageService;

    @BeforeEach
    void setUp() {
        promotionImageService = new PromotionImageService(
                imageDownloadService,
                htmlTemplateService,
                browserPool
        );
    }

    @Test
    @DisplayName("홍보물 이미지 생성이 정상적으로 완료된다")
    void shouldGeneratePromotionImageSuccessfully() throws Exception {
        // given
        PromotionResult promotionResult = createTestPromotionResult();
        BufferedImage mockBackgroundImage = new BufferedImage(1200, 630, BufferedImage.TYPE_INT_RGB);
        String mockHtmlTemplate = "<html><body>Mock Template</body></html>";

        when(imageDownloadService.downloadImage(anyString())).thenReturn(mockBackgroundImage);
        when(htmlTemplateService.generateHtmlTemplate(any(PromotionResult.class))).thenReturn(mockHtmlTemplate);

        // when
        CompletableFuture<byte[]> result = promotionImageService.generatePromotionImage(promotionResult);

        // then
        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> result.isDone());

        assertThat(result.isCompletedExceptionally()).isFalse();
        byte[] imageData = result.get();
        assertThat(imageData).isNotNull();
        assertThat(imageData.length).isGreaterThan(0);
    }

    @Test
    @DisplayName("배경 이미지 다운로드 실패 시 기본 이미지를 사용한다")
    void shouldUseDefaultImageWhenBackgroundDownloadFails() throws Exception {
        // given
        PromotionResult promotionResult = createTestPromotionResult();
        String mockHtmlTemplate = "<html><body>Mock Template</body></html>";

        when(imageDownloadService.downloadImage(anyString()))
                .thenThrow(new RuntimeException("이미지 다운로드 실패"));
        when(htmlTemplateService.generateHtmlTemplate(any(PromotionResult.class))).thenReturn(mockHtmlTemplate);

        // when
        CompletableFuture<byte[]> result = promotionImageService.generatePromotionImage(promotionResult);

        // then
        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> result.isDone());

        // 기본 이미지로 폴백되어 정상 완료되어야 함
        assertThat(result.isCompletedExceptionally()).isFalse();
        byte[] imageData = result.get();
        assertThat(imageData).isNotNull();
    }

    @Test
    @DisplayName("HTML 템플릿 생성 실패 시 예외가 발생한다")
    void shouldThrowExceptionWhenHtmlTemplateGenerationFails() throws Exception {
        // given
        PromotionResult promotionResult = createTestPromotionResult();
        BufferedImage mockBackgroundImage = new BufferedImage(1200, 630, BufferedImage.TYPE_INT_RGB);

        when(imageDownloadService.downloadImage(anyString())).thenReturn(mockBackgroundImage);
        when(htmlTemplateService.generateHtmlTemplate(any(PromotionResult.class)))
                .thenThrow(new RuntimeException("HTML 템플릿 생성 실패"));

        // when
        CompletableFuture<byte[]> result = promotionImageService.generatePromotionImage(promotionResult);

        // then
        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> result.isDone());

        assertThat(result.isCompletedExceptionally()).isTrue();
        assertThatThrownBy(() -> result.get())
                .hasCauseInstanceOf(RuntimeException.class)
                .hasMessageContaining("홍보물 이미지 생성 실패");
    }

    @Test
    @DisplayName("캐시된 이미지가 정상적으로 반환된다")
    void shouldReturnCachedImageWhenAvailable() throws Exception {
        // given
        PromotionResult promotionResult = createTestPromotionResult();

        // 첫 번째 호출
        BufferedImage mockBackgroundImage = new BufferedImage(1200, 630, BufferedImage.TYPE_INT_RGB);
        String mockHtmlTemplate = "<html><body>Mock Template</body></html>";

        when(imageDownloadService.downloadImage(anyString())).thenReturn(mockBackgroundImage);
        when(htmlTemplateService.generateHtmlTemplate(any(PromotionResult.class))).thenReturn(mockHtmlTemplate);

        // when
        CompletableFuture<byte[]> firstResult = promotionImageService.generatePromotionImage(promotionResult);
        CompletableFuture<byte[]> secondResult = promotionImageService.generatePromotionImage(promotionResult);

        // then
        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> firstResult.isDone() && secondResult.isDone());

        assertThat(firstResult.get()).isEqualTo(secondResult.get());
    }

    private PromotionResult createTestPromotionResult() {
        return new PromotionResult(
                "테스트 카페",
                "https://example.com/image.jpg",
                List.of("커피", "분위기", "맛집"),
                List.of("행복", "만족"),
                "감성 가득한 동네 카페에서의 특별한 시간",
                "직접 로스팅한 원두로 내린 커피가 일품입니다. 아늑한 인테리어와 친절한 서비스가 인상적입니다."
        );
    }
}