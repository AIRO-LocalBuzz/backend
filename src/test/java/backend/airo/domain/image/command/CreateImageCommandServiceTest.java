package backend.airo.domain.image.command;

import backend.airo.domain.image.Image;
import backend.airo.domain.image.exception.InvalidImageException;
import backend.airo.domain.image.exception.UnsupportedFormatException;
import backend.airo.domain.image.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateImageCommandService 테스트")
class CreateImageCommandServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private CreateImageCommandService createImageCommandService;

    private Image validJpegImage;

    @BeforeEach
    void setUp() {
        validJpegImage = createImageWithMimeType("https://example.com/image.jpg", "image/jpeg");
    }

    // 정상 케이스
    @Test
    @DisplayName("TC-001: 유효한 JPEG 이미지 저장 성공")
    void tc001_handle_ValidJpegImage_Success() {
        // Given
        when(imageRepository.save(any(Image.class))).thenReturn(validJpegImage);

        // When
        Image result = createImageCommandService.handle(validJpegImage);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getImageUrl()).isEqualTo("https://example.com/image.jpg");
        assertThat(result.getMimeType()).isEqualTo("image/jpeg");
        verify(imageRepository, times(1)).save(validJpegImage);
    }

    // Null 검증
    @Test
    @DisplayName("TC-002: NULL 이미지 처리 - InvalidImageException 발생")
    void tc002_handle_NullImage_ThrowsException() {
        // Given
        Image nullImage = null;

        // When & Then
        assertThatThrownBy(() -> createImageCommandService.handle(nullImage))
                .isInstanceOf(InvalidImageException.class)
                .hasMessageContaining("이미지 정보는 필수입니다");

        verify(imageRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-003: NULL URL 처리 - InvalidImageException 발생")
    void tc003_handle_NullUrl_ThrowsException() {
        // Given
        Image imageWithNullUrl = createImageWithMimeType(null, "image/jpeg");

        // When & Then
        assertThatThrownBy(() -> createImageCommandService.handle(imageWithNullUrl))
                .isInstanceOf(InvalidImageException.class)
                .hasMessageContaining("이미지 URL은 필수입니다");

        verify(imageRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-004: 빈 URL 처리 - InvalidImageException 발생")
    void tc004_handle_EmptyUrl_ThrowsException() {
        // Given
        Image imageWithEmptyUrl = createImageWithMimeType("", "image/jpeg");

        // When & Then
        assertThatThrownBy(() -> createImageCommandService.handle(imageWithEmptyUrl))
                .isInstanceOf(InvalidImageException.class)
                .hasMessageContaining("이미지 URL은 필수입니다");

        verify(imageRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-005: 공백 URL 처리 - InvalidImageException 발생")
    void tc005_handle_BlankUrl_ThrowsException() {
        // Given
        Image imageWithBlankUrl = createImageWithMimeType("   ", "image/jpeg");

        // When & Then
        assertThatThrownBy(() -> createImageCommandService.handle(imageWithBlankUrl))
                .isInstanceOf(InvalidImageException.class)
                .hasMessageContaining("이미지 URL은 필수입니다");

        verify(imageRepository, never()).save(any());
    }

    // MIME 타입 검증
    @Test
    @DisplayName("TC-006: 지원하지 않는 MIME 타입 - UnsupportedFormatException 발생")
    void tc006_handle_UnsupportedMimeType_ThrowsException() {
        // Given
        Image imageWithUnsupportedMime = createImageWithMimeType("https://example.com/image.bmp", "image/bmp");

        // When & Then
        assertThatThrownBy(() -> createImageCommandService.handle(imageWithUnsupportedMime))
                .isInstanceOf(UnsupportedFormatException.class)
                .hasMessageContaining("지원하지 않는 형식: image/bmp");

        verify(imageRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-007: NULL MIME 타입 - InvalidImageException 발생")
    void tc007_handle_NullMimeType_ThrowsException() {
        // Given
        Image imageWithNullMime = createImageWithMimeType("https://example.com/image.jpg", null);

        // When & Then
        assertThatThrownBy(() -> createImageCommandService.handle(imageWithNullMime))
                .isInstanceOf(InvalidImageException.class)
                .hasMessageContaining("이미지 MIME 타입은 필수입니다");

        verify(imageRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-008: 잘못된 MIME 형식 - UnsupportedFormatException 발생")
    void tc008_handle_InvalidMimeFormat_ThrowsException() {
        // Given
        Image imageWithInvalidMime = createImageWithMimeType("https://example.com/file.txt", "text/plain");

        // When & Then
        assertThatThrownBy(() -> createImageCommandService.handle(imageWithInvalidMime))
                .isInstanceOf(UnsupportedFormatException.class)
                .hasMessageContaining("지원하지 않는 형식: text/plain");

        verify(imageRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-009: 대소문자 혼합 MIME - 정상 처리")
    void tc009_handle_MixedCaseMimeType_Success() {
        // Given
        Image imageWithMixedCaseMime = createImageWithMimeType("https://example.com/image.jpg", "IMAGE/JPEG");
        when(imageRepository.save(any(Image.class))).thenReturn(imageWithMixedCaseMime);

        // When
        Image result = createImageCommandService.handle(imageWithMixedCaseMime);

        // Then
        assertThat(result).isNotNull();
        verify(imageRepository, times(1)).save(imageWithMixedCaseMime);
    }

    // 재시도 로직
    @Test
    @DisplayName("TC-010: 첫 번째 시도 성공")
    void tc010_handleWithRetry_FirstAttemptSuccess() {
        // Given
        when(imageRepository.save(any(Image.class))).thenReturn(validJpegImage);

        // When
        Image result = createImageCommandService.handleWithRetry(validJpegImage);

        // Then
        assertThat(result).isNotNull();
        verify(imageRepository, times(1)).save(validJpegImage);
    }

    @Test
    @DisplayName("TC-011: 2번째 시도 성공")
    void tc011_handleWithRetry_SecondAttemptSuccess() {
        // Given
        when(imageRepository.save(any(Image.class)))
                .thenThrow(new RuntimeException("DB 연결 실패"))
                .thenReturn(validJpegImage);

        // When
        Image result = createImageCommandService.handleWithRetry(validJpegImage);

        // Then
        assertThat(result).isNotNull();
        verify(imageRepository, times(2)).save(validJpegImage);
    }

    @Test
    @DisplayName("TC-012: 3번째 시도 성공")
    void tc012_handleWithRetry_ThirdAttemptSuccess() {
        // Given
        when(imageRepository.save(any(Image.class)))
                .thenThrow(new RuntimeException("DB 연결 실패"))
                .thenThrow(new RuntimeException("네트워크 오류"))
                .thenReturn(validJpegImage);

        // When
        Image result = createImageCommandService.handleWithRetry(validJpegImage);

        // Then
        assertThat(result).isNotNull();
        verify(imageRepository, times(3)).save(validJpegImage);
    }

    @Test
    @DisplayName("TC-013: 모든 시도 실패 - RuntimeException 발생")
    void tc013_handleWithRetry_AllAttemptsFail_ThrowsException() {
        // Given
        when(imageRepository.save(any(Image.class)))
                .thenThrow(new RuntimeException("지속적인 DB 오류"));

        // When & Then
        assertThatThrownBy(() -> createImageCommandService.handleWithRetry(validJpegImage))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("최대 재시도 횟수 초과");

        verify(imageRepository, times(3)).save(validJpegImage);
    }

    // 동시성 제어
    @Test
    @DisplayName("TC-014: 단일 스레드 처리")
    void tc014_handleWithLock_SingleThread_Success() {
        // Given
        when(imageRepository.save(any(Image.class))).thenReturn(validJpegImage);

        // When
        Image result = createImageCommandService.handleWithLock(validJpegImage);

        // Then
        assertThat(result).isNotNull();
        verify(imageRepository, times(1)).save(validJpegImage);
    }

    @Test
    @DisplayName("TC-015: 멀티 스레드 동시 접근 - 순차적 처리")
    void tc015_handleWithLock_MultiThread_SerialExecution() throws InterruptedException {
        // Given
        int threadCount = 5;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        when(imageRepository.save(any(Image.class))).thenAnswer(invocation -> {
            Thread.sleep(100); // 처리 시간 시뮬레이션
            return validJpegImage;
        });

        // When
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    startLatch.await();
                    createImageCommandService.handleWithLock(validJpegImage);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    // 예외 처리
                } finally {
                    endLatch.countDown();
                }
            }).start();
        }

        startLatch.countDown();
        boolean completed = endLatch.await(10, TimeUnit.SECONDS);

        // Then
        assertThat(completed).isTrue();
        assertThat(successCount.get()).isEqualTo(threadCount);
        verify(imageRepository, times(threadCount)).save(validJpegImage);
    }

    @Test
    @DisplayName("TC-016: 락 해제 보장 - 예외 발생 시에도 락 해제")
    void tc016_handleWithLock_ExceptionOccurs_LockReleased() {
        // Given
        when(imageRepository.save(any(Image.class)))
                .thenThrow(new RuntimeException("저장 중 오류 발생"));

        // When & Then
        assertThatThrownBy(() -> createImageCommandService.handleWithLock(validJpegImage))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("저장 중 오류 발생");

        // 락이 해제되었는지 확인하기 위해 다시 호출
        when(imageRepository.save(any(Image.class))).thenReturn(validJpegImage);

        assertThatCode(() -> createImageCommandService.handleWithLock(validJpegImage))
                .doesNotThrowAnyException();
    }




    // 헬퍼 메소드
    private Image createImageWithMimeType(String imageUrl, String mimeType) {
        return new Image(1L, imageUrl, mimeType);
    }

}