package backend.airo.infra.promotion;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 테스트용 이미지 생성 및 검증 헬퍼 클래스
 */
public class ImageTestHelper {

    private static final String TEST_OUTPUT_DIR = "build/test-outputs/images";

    /**
     * 테스트용 이미지 데이터를 파일로 저장
     */
    public static void saveImageToFile(byte[] imageData, String testName) {
        try {
            // 출력 디렉토리 생성
            Path outputDir = Paths.get(TEST_OUTPUT_DIR);
            Files.createDirectories(outputDir);

            // 파일명 생성 (타임스탬프 포함)
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = String.format("%s_%s.png", testName, timestamp);
            Path filePath = outputDir.resolve(fileName);

            // 파일 저장
            Files.write(filePath, imageData);

            System.out.println("테스트 이미지 저장됨: " + filePath.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("테스트 이미지 저장 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 이미지 데이터의 메타정보 출력
     */
    public static void printImageInfo(byte[] imageData, String testName) {
        try {
            System.out.println("=== " + testName + " 이미지 정보 ===");
            System.out.println("데이터 크기: " + imageData.length + " bytes");

            // PNG 헤더 확인
            if (imageData.length >= 8) {
                boolean isPNG = imageData[0] == (byte) 0x89 &&
                        imageData[1] == (byte) 0x50 &&
                        imageData[2] == (byte) 0x4E &&
                        imageData[3] == (byte) 0x47;
                System.out.println("PNG 형식: " + isPNG);

                if (isPNG && imageData.length >= 24) {
                    // PNG 헤더에서 width, height 추출 (IHDR 청크)
                    int width = ((imageData[16] & 0xFF) << 24) |
                            ((imageData[17] & 0xFF) << 16) |
                            ((imageData[18] & 0xFF) << 8) |
                            (imageData[19] & 0xFF);
                    int height = ((imageData[20] & 0xFF) << 24) |
                            ((imageData[21] & 0xFF) << 16) |
                            ((imageData[22] & 0xFF) << 8) |
                            (imageData[23] & 0xFF);
                    System.out.println("이미지 크기: " + width + "x" + height);
                }
            }

            // 데이터 샘플 출력 (처음 20바이트)
            StringBuilder hexData = new StringBuilder();
            int limit = Math.min(20, imageData.length);
            for (int i = 0; i < limit; i++) {
                hexData.append(String.format("%02X ", imageData[i] & 0xFF));
            }
            System.out.println("데이터 샘플: " + hexData.toString());

        } catch (Exception e) {
            System.err.println("이미지 정보 분석 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 테스트용 실제 이미지 생성 (Mock 대신 사용)
     */
    public static byte[] createTestImage(String text, int width, int height) {
        try {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();

            // 안티앨리어싱 설정
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // 배경 (그라데이션)
            GradientPaint gradient = new GradientPaint(0, 0, Color.BLUE, width, height, Color.CYAN);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, width, height);

            // 테두리
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(10, 10, width - 20, height - 20);

            // 메인 텍스트
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();
            int x = (width - textWidth) / 2;
            int y = (height + textHeight) / 2;
            g2d.drawString(text, x, y);

            // 타임스탬프 추가
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString(timestamp, 20, height - 20);

            // 테스트 정보 추가
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.drawString("Size: " + width + "x" + height, 20, 30);

            g2d.dispose();

            // PNG로 변환
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);

            System.out.println("테스트 이미지 생성 완료: " + text + " (" + width + "x" + height + ")");
            return baos.toByteArray();

        } catch (Exception e) {
            System.err.println("테스트 이미지 생성 실패: " + e.getMessage());
            e.printStackTrace();
            return new byte[0];
        }
    }

    /**
     * HTML 템플릿을 파일로 저장
     */
    public static void saveHtmlToFile(String htmlContent, String testName) {
        try {
            Path outputDir = Paths.get(TEST_OUTPUT_DIR);
            Files.createDirectories(outputDir);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = String.format("%s_%s.html", testName, timestamp);
            Path filePath = outputDir.resolve(fileName);

            Files.write(filePath, htmlContent.getBytes());

            System.out.println("HTML 템플릿 저장됨: " + filePath.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("HTML 템플릿 저장 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 이미지 파일 형식 검증
     */
    public static boolean isValidPngImage(byte[] imageData) {
        if (imageData == null || imageData.length < 8) {
            return false;
        }

        return imageData[0] == (byte) 0x89 &&
                imageData[1] == (byte) 0x50 &&
                imageData[2] == (byte) 0x4E &&
                imageData[3] == (byte) 0x47 &&
                imageData[4] == (byte) 0x0D &&
                imageData[5] == (byte) 0x0A &&
                imageData[6] == (byte) 0x1A &&
                imageData[7] == (byte) 0x0A;
    }
}