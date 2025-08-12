//package backend.airo.application.image.usecase;
//
//import backend.airo.domain.image.Image;
//import backend.airo.domain.image.command.CreateImageCommandService;
//import backend.airo.domain.image.exception.ImageErrorCode;
//import backend.airo.domain.image.exception.InvalidImageException;
//import backend.airo.domain.image.exception.UnsupportedFormatException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("ImageCreateUseCase 테스트")
//class ImageCreateUseCaseTest {
//
//    @Mock
//    private CreateImageCommandService createImageCommandService;
//
//    @InjectMocks
//    private ImageCreateUseCase imageCreateUseCase;
//
//    private Image validJpegImage;
//    private Image validPngImage;
//    private Image validGifImage;
//
//    @BeforeEach
//    void setUp() {
//        validJpegImage = createImageWithMimeType("https://example.com/image1.jpg", "image/jpeg");
//        validPngImage = createImageWithMimeType("https://example.com/image2.png", "image/png");
//        validGifImage = createImageWithMimeType("https://example.com/image3.gif", "image/gif");
//    }
//
//    // 단일 이미지 업로드 테스트
//    @Test
//    @DisplayName("TC-001: 단일 이미지 업로드 성공")
//    void tc001_uploadSingleImage_ValidImage_Success() {
//        // Given
//        when(createImageCommandService.handle(validJpegImage)).thenReturn(validJpegImage);
//
//        // When
//        Image result = imageCreateUseCase.uploadSingleImage(validJpegImage);
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result.getImageUrl()).isEqualTo("https://example.com/image1.jpg");
//        assertThat(result.getMimeType()).isEqualTo("image/jpeg");
//        verify(createImageCommandService, times(1)).handle(validJpegImage);
//    }
//
//    @Test
//    @DisplayName("TC-002: 단일 이미지 업로드 실패 - InvalidImageException")
//    void tc002_uploadSingleImage_InvalidImage_ThrowsException() {
//        // Given
//        String mimeType = "image/bmp"; // 지원하지 않는 MIME 타입
//        Image invalidImage = createImageWithMimeType("https://example.com/image1.jpg", mimeType);
//        when(createImageCommandService.handle(invalidImage))
//                .thenThrow(new UnsupportedFormatException(mimeType, SUPPORTED_MIME_TYPES.toArray(new String[0])));
//
//        // When & Then
//        assertThatThrownBy(() -> imageCreateUseCase.uploadSingleImage(invalidImage))
//                .isInstanceOf(UnsupportedFormatException.class);
//
//        verify(createImageCommandService, times(1)).handle(invalidImage);
//    }
//
//    // 다중 이미지 업로드 테스트
//    @Test
//    @DisplayName("TC-003: 다중 이미지 업로드 성공")
//    void tc003_uploadMultipleImages_ValidImages_Success() {
//        // Given
//        List<Image> inputImages = Arrays.asList(validJpegImage, validPngImage, validGifImage);
//        when(createImageCommandService.handle(validJpegImage)).thenReturn(validJpegImage);
//        when(createImageCommandService.handle(validPngImage)).thenReturn(validPngImage);
//        when(createImageCommandService.handle(validGifImage)).thenReturn(validGifImage);
//
//        // When
//        List<Image> results = imageCreateUseCase.uploadMultipleImages(inputImages);
//
//        // Then
//        assertThat(results).hasSize(3);
//        assertThat(results).containsExactly(validJpegImage, validPngImage, validGifImage);
//        verify(createImageCommandService, times(1)).handle(validJpegImage);
//        verify(createImageCommandService, times(1)).handle(validPngImage);
//        verify(createImageCommandService, times(1)).handle(validGifImage);
//    }
//
//
//
//    @Test
//    @DisplayName("TC-005: 다중 이미지 업로드 - 일부 실패")
//    void tc005_uploadMultipleImages_PartialFailure_ThrowsException() {
//        // Given
//        Image invalidImage = createImageWithMimeType("https://example.com/invalid.bmp", "image/bmp");
//        List<Image> inputImages = Arrays.asList(validJpegImage, invalidImage);
//
//        when(createImageCommandService.handle(validJpegImage)).thenReturn(validJpegImage);
//        when(createImageCommandService.handle(invalidImage))
//                .thenThrow(new UnsupportedFormatException("image/bmp", new String[]{"image/jpeg", "image/png"}));
//
//        // When & Then
//        assertThatThrownBy(() -> imageCreateUseCase.uploadMultipleImages(inputImages))
//                .isInstanceOf(UnsupportedFormatException.class);
//
//        verify(createImageCommandService, times(1)).handle(validJpegImage);
//        verify(createImageCommandService, times(1)).handle(invalidImage);
//    }
//
//    @Test
//    @DisplayName("TC-006: 다중 이미지 업로드 - 단일 이미지")
//    void tc006_uploadMultipleImages_SingleImage_Success() {
//        // Given
//        List<Image> singleImageList = Collections.singletonList(validJpegImage);
//        when(createImageCommandService.handle(validJpegImage)).thenReturn(validJpegImage);
//
//        // When
//        List<Image> results = imageCreateUseCase.uploadMultipleImages(singleImageList);
//
//        // Then
//        assertThat(results).hasSize(1);
//        assertThat(results.get(0)).isEqualTo(validJpegImage);
//        verify(createImageCommandService, times(1)).handle(validJpegImage);
//    }
//
//
//
//
//    // 락 기반 업로드 테스트
//    @Test
//    @DisplayName("TC-009: 락 기반 이미지 업로드 성공")
//    void tc009_uploadImageWithLock_ValidImage_Success() {
//        // Given
//        when(createImageCommandService.handleWithLock(validJpegImage)).thenReturn(validJpegImage);
//
//        // When
//        Image result = imageCreateUseCase.uploadImageWithLock(validJpegImage);
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result).isEqualTo(validJpegImage);
//        verify(createImageCommandService, times(1)).handleWithLock(validJpegImage);
//    }
//
//    @Test
//    @DisplayName("TC-010: 락 기반 이미지 업로드 실패")
//    void tc010_uploadImageWithLock_InvalidImage_ThrowsException() {
//        // Given
//        Image invalidImage = createImageWithMimeType(null, "image/jpeg");
//        when(createImageCommandService.handleWithLock(invalidImage))
//                .thenThrow(new InvalidImageException(null, "imageUrl", "null"));
//
//        // When & Then
//        assertThatThrownBy(() -> imageCreateUseCase.uploadImageWithLock(invalidImage))
//                .isInstanceOf(InvalidImageException.class);
//
//        verify(createImageCommandService, times(1)).handleWithLock(invalidImage);
//    }
//
//    // 경계값 테스트
//    @Test
//    @DisplayName("TC-011: 대용량 이미지 리스트 처리")
//    void tc011_uploadMultipleImages_LargeList_Success() {
//        // Given
//        int imageCount = 100;
//        List<Image> largeImageList = Collections.nCopies(imageCount, validJpegImage);
//        when(createImageCommandService.handle(any(Image.class))).thenReturn(validJpegImage);
//
//        // When
//        List<Image> results = imageCreateUseCase.uploadMultipleImages(largeImageList);
//
//        // Then
//        assertThat(results).hasSize(imageCount);
//        verify(createImageCommandService, times(imageCount)).handle(validJpegImage);
//    }
//
//
//    // 비즈니스 로직 검증
//    @Test
//    @DisplayName("TC-013: 모든 업로드 방식에서 동일한 이미지 처리 결과")
//    void tc013_allUploadMethods_SameImage_ConsistentResults() {
//        // Given - 각각 다른 이미지 객체 생성하여 호출 횟수 충돌 방지
//        Image singleImage = createImageWithMimeType("https://example.com/single.jpg", "image/jpeg");
//        Image retryImage = createImageWithMimeType("https://example.com/retry.jpg", "image/jpeg");
//        Image lockImage = createImageWithMimeType("https://example.com/lock.jpg", "image/jpeg");
//        Image multipleImage = createImageWithMimeType("https://example.com/multiple.jpg", "image/jpeg");
//
//        when(createImageCommandService.handle(singleImage)).thenReturn(singleImage);
//        when(createImageCommandService.handleWithLock(lockImage)).thenReturn(lockImage);
//        when(createImageCommandService.handle(multipleImage)).thenReturn(multipleImage);
//
//        // When
//        Image singleResult = imageCreateUseCase.uploadSingleImage(singleImage);
//        Image lockResult = imageCreateUseCase.uploadImageWithLock(lockImage);
//        List<Image> multipleResult = imageCreateUseCase.uploadMultipleImages(Collections.singletonList(multipleImage));
//
//        // Then - 각각의 결과가 올바른지 검증
//        assertThat(singleResult).isNotNull();
//        assertThat(singleResult.getImageUrl()).isEqualTo("https://example.com/single.jpg");
//        assertThat(singleResult.getMimeType()).isEqualTo("image/jpeg");
//
//        assertThat(lockResult).isNotNull();
//        assertThat(lockResult.getImageUrl()).isEqualTo("https://example.com/lock.jpg");
//        assertThat(lockResult.getMimeType()).isEqualTo("image/jpeg");
//
//        assertThat(multipleResult).hasSize(1);
//        assertThat(multipleResult.get(0)).isNotNull();
//        assertThat(multipleResult.get(0).getImageUrl()).isEqualTo("https://example.com/multiple.jpg");
//        assertThat(multipleResult.get(0).getMimeType()).isEqualTo("image/jpeg");
//
//        // 각 메소드가 정확히 한 번씩 호출되었는지 검증
//        verify(createImageCommandService, times(1)).handle(singleImage);
//        verify(createImageCommandService, times(1)).handleWithLock(lockImage);
//        verify(createImageCommandService, times(1)).handle(multipleImage);
//    }
//
//
//    // 헬퍼 메소드
//    private Image createImageWithMimeType(String imageUrl, String mimeType) {
//        return new Image(1L, 1L, imageUrl, mimeType);
//    }
//
//    // 지원하는 MIME 타입들
//    private static final List<String> SUPPORTED_MIME_TYPES = Arrays.asList(
//            "image/jpeg",
//            "image/jpg",
//            "image/png",
//            "image/gif"
//    );
//}