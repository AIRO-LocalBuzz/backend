package backend.airo.infra.promotion.service;

import backend.airo.domain.promotion.PromotionResult;
import backend.airo.infra.promotion.PlaywrightBrowserPool;
import backend.airo.support.cache.local.CacheName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.cache.CacheManager;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.ScreenshotType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionImageService {

    private final backend.airo.infra.promotion.service.ImageDownloadService imageDownloadService;
    private final backend.airo.infra.promotion.service.HtmlTemplateService htmlTemplateService;
    private final PlaywrightBrowserPool browserPool;
    private final CacheManager cacheManager;

    /**
     * 홍보물 이미지 생성 (비동기)
     */
    public CompletableFuture<byte[]> generatePromotionImage(PromotionResult promotionResult, Long postId) {  // postId 추가
        return CompletableFuture.supplyAsync(() -> {
            try {
                String cacheKey = "post_" + postId;  // postId 기반 캐시 키

                // 캐시에서 먼저 확인
                var cache = cacheManager.getCache(CacheName.PROMOTION_THUMBNAILS_CACHE);
                if (cache != null) {
                    byte[] cachedResult = cache.get(cacheKey, byte[].class);
                    if (cachedResult != null) {
                        log.info("캐시에서 홍보물 이미지 반환: {}", promotionResult.spotName());
                        return cachedResult;
                    }
                }

                log.info("홍보물 이미지 생성 시작: {}", promotionResult.spotName());

                // 배경 이미지 다운로드 (캐시 적용됨)
                BufferedImage backgroundImage = downloadBackgroundImage(promotionResult.mainImageUrl());

                // HTML 템플릿 생성
                String htmlContent = htmlTemplateService.generateHtmlTemplate(promotionResult);

                // 텍스트 오버레이 생성
                BufferedImage textOverlay = generateTextOverlay(htmlContent);

                // 이미지 합성
                BufferedImage finalImage = compositeImages(backgroundImage, textOverlay);

                // PNG 바이트 배열로 변환
                byte[] result = imageToByteArray(finalImage);

                // 캐시에 저장
                if (cache != null) {
                    cache.put(cacheKey, result);
                    log.info("홍보물 이미지 캐시에 저장: postId={}, spotName={}", postId, promotionResult.spotName());
                }

                log.info("홍보물 이미지 생성 완료: {} bytes", result.length);
                return result;

            } catch (Exception e) {
                log.error("홍보물 이미지 생성 실패: {}", promotionResult.spotName(), e);
                throw new RuntimeException("홍보물 이미지 생성 실패", e);
            }
        });
    }

    /**
     * 배경 이미지 다운로드 및 크기 조정
     */
    private BufferedImage downloadBackgroundImage(String imageUrl) {
        try {
            BufferedImage originalImage = imageDownloadService.downloadImage(imageUrl);
            return resizeImageToStandard(originalImage);
        } catch (Exception e) {
            log.warn("배경 이미지 다운로드 실패, 기본 이미지 사용: {}", imageUrl, e);
            return createDefaultBackgroundImage();
        }
    }

    /**
     * HTML을 투명 배경 텍스트 이미지로 변환 (개선된 버전)
     */
    /**
     * HTML을 투명 배경 텍스트 이미지로 변환 (개선된 버전)
     */
    private BufferedImage generateTextOverlay(String htmlContent) throws Exception {
        return browserPool.executeWithBrowser(browser -> {
            try {
                var page = browser.newPage();

                // 페이지 설정 최적화
                page.setViewportSize(1200, 630);
                page.setExtraHTTPHeaders(Map.of(
                        "Accept-Language", "ko-KR,ko;q=0.9,en;q=0.8"
                ));

                // HTML 콘텐츠 설정
                page.setContent(htmlContent);

                // 폰트 로딩 및 렌더링 완료 대기
                page.waitForLoadState(LoadState.NETWORKIDLE);
                page.waitForTimeout(2000); // 폰트 로딩 추가 대기

                // CSS 애니메이션이 있다면 완료 대기
                page.evaluate("() => new Promise(resolve => requestAnimationFrame(resolve))");

                // 투명 배경 스크린샷 옵션 설정
                var screenshotOptions = new Page.ScreenshotOptions()
                        .setOmitBackground(true)
                        .setType(ScreenshotType.PNG)
                        .setClip(0, 0, 1200, 630); // 정확한 크기로 클리핑

                byte[] pngBytes = page.screenshot(screenshotOptions);
                page.close();

                BufferedImage image = ImageIO.read(new ByteArrayInputStream(pngBytes));
                if (image == null) {
                    throw new RuntimeException("스크린샷에서 이미지 생성 실패");
                }

                log.debug("텍스트 오버레이 생성 완료: {}x{}", image.getWidth(), image.getHeight());
                return image;

            } catch (IOException e) {
                throw new RuntimeException("텍스트 오버레이 생성 실패", e);
            }
        });
    }

    /**
     * 배경 이미지와 텍스트 오버레이 합성
     */
    private BufferedImage compositeImages(BufferedImage background, BufferedImage overlay) {
        int width = background.getWidth();
        int height = background.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = result.createGraphics();

        try {
            // 고품질 렌더링 설정
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 배경 이미지 그리기
            g2d.drawImage(background, 0, 0, null);

            // 텍스트 오버레이 중앙 정렬하여 합성
            int overlayX = (width - overlay.getWidth()) / 2;
            int overlayY = (height - overlay.getHeight()) / 2;
            g2d.drawImage(overlay, overlayX, overlayY, null);

        } finally {
            g2d.dispose();
        }

        return result;
    }

    /**
     * 이미지를 표준 크기(1200x630)로 조정
     */
    private BufferedImage resizeImageToStandard(BufferedImage originalImage) {
        int targetWidth = 1200;
        int targetHeight = 630;

        BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();

        try {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        } finally {
            g2d.dispose();
        }

        return resized;
    }

    /**
     * 기본 배경 이미지 생성 (폴백용)
     */
    private BufferedImage createDefaultBackgroundImage() {
        BufferedImage defaultImage = new BufferedImage(1200, 630, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = defaultImage.createGraphics();

        try {
            // 그라디언트 배경 생성
            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(74, 144, 226),
                    1200, 630, new Color(80, 201, 195)
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, 1200, 630);
        } finally {
            g2d.dispose();
        }

        return defaultImage;
    }

    /**
     * BufferedImage를 PNG 바이트 배열로 변환
     */
    private byte[] imageToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        return baos.toByteArray();
    }
}