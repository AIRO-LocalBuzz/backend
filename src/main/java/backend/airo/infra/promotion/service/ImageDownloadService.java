package backend.airo.infra.promotion.service;

import backend.airo.support.cache.local.CacheName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageDownloadService {

    private final WebClient webClient;
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final Duration TIMEOUT = Duration.ofSeconds(30);

    /**
     * URL에서 이미지 다운로드 (BufferedImage 반환)
     */
    public BufferedImage downloadImage(String imageUrl) {
        byte[] cachedBytes = downloadImageBytes(imageUrl);
        try {
            return ImageIO.read(new ByteArrayInputStream(cachedBytes));
        } catch (Exception e) {
            log.error("캐시된 이미지 바이트를 BufferedImage로 변환 실패: {}", imageUrl, e);
            return createDefaultImage();
        }
    }

    /**
     * URL에서 이미지 바이트 다운로드 (캐싱 적용)
     */
    @Cacheable(value = CacheName.DOWNLOADED_IMAGES_CACHE, key = "#imageUrl")
    public byte[] downloadImageBytes(String imageUrl) {
        try {
            log.info("원본 URL: {}", imageUrl);
            
            // 🔧 URL 유효성 사전 검증
            if (!isValidImageUrl(imageUrl)) {
                log.warn("유효하지 않은 이미지 URL: {}", imageUrl);
                return createDefaultImageBytes();
            }
            
            // 🔧 여러 URL 변형 시도
            String[] urlVariants = generateUrlVariants(imageUrl);
            
            for (String url : urlVariants) {
                try {
                    log.info("이미지 다운로드 시도: {}", url);
                    
                    byte[] imageBytes = webClient.get()
                            .uri(url)
                            .retrieve()
                            .bodyToMono(byte[].class)
                            .timeout(TIMEOUT)
                            .block();

                    if (imageBytes != null && imageBytes.length > 0) {
                        log.info("이미지 다운로드 성공: {} ({} bytes)", url, imageBytes.length);
                        return processDownloadedImage(imageBytes, url);
                    }
                    
                } catch (Exception e) {
                    log.debug("URL 시도 실패: {} - {}", url, e.getMessage());
                }
            }
            
            log.warn("모든 URL 변형 시도 실패: {}", imageUrl);
            return createDefaultImageBytes();
            
        } catch (Exception e) {
            log.warn("이미지 다운로드 실패, 기본 이미지로 대체: {}", imageUrl);
            return createDefaultImageBytes();
        }
    }

    /**
     * 이미지 품질 최적화 (필요시)
     */
    public BufferedImage optimizeImage(BufferedImage originalImage, int quality) {
        // 현재는 원본 반환, 추후 압축 로직 추가 가능
        return originalImage;
    }

    /**
     * 기본 이미지 생성 (다운로드 실패 시 사용)
     */
    private BufferedImage createDefaultImage() {
        log.info("기본 이미지 생성");
        BufferedImage defaultImage = new BufferedImage(1200, 630, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = defaultImage.createGraphics();

        // 안티 앨리어싱 설정
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 그라데이션 배경 생성
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(52, 152, 219),
                1200, 630, new Color(155, 89, 182)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 1200, 630);

        // 중앙에 텍스트 추가
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Sans-serif", Font.BOLD, 48));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "AIRO LOCAL BUZZ";
        int x = (1200 - fm.stringWidth(text)) / 2;
        int y = (630 + fm.getAscent()) / 2;
        g2d.drawString(text, x, y);

        g2d.dispose();
        return defaultImage;
    }

    /**
     * 기본 이미지를 바이트 배열로 반환
     */
    private byte[] createDefaultImageBytes() {
        try {
            BufferedImage defaultImage = createDefaultImage();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(defaultImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("기본 이미지 바이트 생성 실패", e);
            // 최소한의 기본값 반환
            return new byte[0];
        }
    }

    /**
     * 이미지 형식 검증
     */
    public boolean isValidImageFormat(String contentType) {
        return contentType != null && (
                contentType.startsWith("image/jpeg") ||
                        contentType.startsWith("image/jpg") ||
                        contentType.startsWith("image/png") ||
                        contentType.startsWith("image/webp")
        );
    }

    /**
     * URL 유효성 검증
     */
    private boolean isValidImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return false;
        }
        
        if (!imageUrl.startsWith("http")) {
            return false;
        }
        
        // 확장자 확인
        String lowerUrl = imageUrl.toLowerCase();
        return lowerUrl.contains(".jpg") || lowerUrl.contains(".jpeg") || 
               lowerUrl.contains(".png") || lowerUrl.contains(".webp");
    }

    /**
     * 여러 URL 변형 생성
     */
    private String[] generateUrlVariants(String originalUrl) {
        try {
            // 1. 원본 URL
            // 2. 한번 디코딩된 URL
            // 3. 두번 디코딩된 URL (이중 인코딩 경우)
            
            String decoded1 = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8);
            String decoded2 = URLDecoder.decode(decoded1, StandardCharsets.UTF_8);
            
            return new String[]{
                originalUrl,
                decoded1,
                decoded2
            };
            
        } catch (Exception e) {
            return new String[]{originalUrl};
        }
    }

    /**
     * 다운로드된 이미지 바이트를 처리하고 유효성 검사
     */
    private byte[] processDownloadedImage(byte[] imageBytes, String url) throws IOException {
        if (imageBytes.length > MAX_FILE_SIZE) {
            throw new RuntimeException("이미지 파일 크기가 너무 큽니다: " + imageBytes.length + " bytes");
        }

        // 🔧 IOException 처리 추가
        try {
            // 이미지 유효성 검사
            BufferedImage testImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (testImage == null) {
                throw new RuntimeException("유효하지 않은 이미지 파일입니다: " + url);
            }

            log.info("이미지 다운로드 완료: {}x{}", testImage.getWidth(), testImage.getHeight());
            return imageBytes;
            
        } catch (IOException e) {
            log.error("이미지 처리 중 오류 발생: {}", url, e);
            throw e; // 상위에서 처리하도록 재던짐
        }
    }
}