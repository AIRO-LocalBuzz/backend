package backend.airo.domain.image.command;

import backend.airo.domain.image.Image;
import backend.airo.domain.image.exception.ImageNotFoundException;
import backend.airo.domain.image.exception.InvalidImageException;
import backend.airo.domain.image.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateImageCommandService 테스트")
class UpdateImageCommandServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private UpdateImageCommandService updateImageCommandService;

    private Image testImage;
    private final Long IMAGE_ID = 1L;

    @BeforeEach
    void setUp() {
        testImage = createImage(IMAGE_ID, "https://example.com/image.jpg", "original caption");
    }

    // 정렬 순서 업데이트 테스트
    @Test
    @DisplayName("TC-001: 정렬 순서 정상 업데이트")
    void tc001_updateSortOrder_ValidInput_Success() {
        // Given
        Integer newSortOrder = 5;
        when(imageRepository.findById(IMAGE_ID)).thenReturn(testImage);
        when(imageRepository.save(any(Image.class))).thenReturn(testImage);

        // When
        Image result = updateImageCommandService.updateSortOrder(IMAGE_ID, newSortOrder);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getSortOrder()).isEqualTo(newSortOrder);
        verify(imageRepository).findById(IMAGE_ID);
        verify(imageRepository).save(testImage);
    }

    @Test
    @DisplayName("TC-002: NULL imageId - InvalidImageException 발생")
    void tc002_updateSortOrder_NullImageId_ThrowsException() {
        // Given
        Long nullImageId = null;
        Integer newSortOrder = 5;

        // When & Then
        assertThatThrownBy(() -> updateImageCommandService.updateSortOrder(nullImageId, newSortOrder))
                .isInstanceOf(InvalidImageException.class);

        verify(imageRepository, never()).findById(any());
    }

    @Test
    @DisplayName("TC-003: NULL sortOrder - IllegalArgumentException 발생")
    void tc003_updateSortOrder_NullSortOrder_ThrowsException() {
        // Given
        Integer nullSortOrder = null;

        // When & Then
        assertThatThrownBy(() -> updateImageCommandService.updateSortOrder(IMAGE_ID, nullSortOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("정렬 순서는 0 이상이어야 합니다");

        verify(imageRepository, never()).findById(any());
    }

    @Test
    @DisplayName("TC-004: 음수 sortOrder - IllegalArgumentException 발생")
    void tc004_updateSortOrder_NegativeSortOrder_ThrowsException() {
        // Given
        Integer negativeSortOrder = -1;

        // When & Then
        assertThatThrownBy(() -> updateImageCommandService.updateSortOrder(IMAGE_ID, negativeSortOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("정렬 순서는 0 이상이어야 합니다");

        verify(imageRepository, never()).findById(any());
    }

    @Test
    @DisplayName("TC-005: 존재하지 않는 이미지 - ImageNotFoundException 발생")
    void tc005_updateSortOrder_NonExistentImage_ThrowsException() {
        // Given
        Long nonExistentId = 999L;
        when(imageRepository.findById(nonExistentId)).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> updateImageCommandService.updateSortOrder(nonExistentId, 5))
                .isInstanceOf(ImageNotFoundException.class);

        verify(imageRepository).findById(nonExistentId);
        verify(imageRepository, never()).save(any());
    }

    // 캡션 업데이트 테스트
    @Test
    @DisplayName("TC-006: 캡션 정상 업데이트")
    void tc006_updateCaption_ValidInput_Success() {
        // Given
        String newCaption = "새로운 캡션";
        when(imageRepository.findById(IMAGE_ID)).thenReturn(testImage);
        when(imageRepository.save(any(Image.class))).thenReturn(testImage);

        // When
        Image result = updateImageCommandService.updateCaption(IMAGE_ID, newCaption);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCaption()).isEqualTo(newCaption);
        verify(imageRepository).findById(IMAGE_ID);
        verify(imageRepository).save(testImage);
    }

    @Test
    @DisplayName("TC-007: 캡션 업데이트 NULL imageId - InvalidImageException 발생")
    void tc007_updateCaption_NullImageId_ThrowsException() {
        // Given
        Long nullImageId = null;
        String newCaption = "캡션";

        // When & Then
        assertThatThrownBy(() -> updateImageCommandService.updateCaption(nullImageId, newCaption))
                .isInstanceOf(InvalidImageException.class);

        verify(imageRepository, never()).findById(any());
    }

    @Test
    @DisplayName("TC-008: NULL 캡션으로 업데이트 - 정상 처리")
    void tc008_updateCaption_NullCaption_Success() {
        // Given
        String nullCaption = null;
        when(imageRepository.findById(IMAGE_ID)).thenReturn(testImage);
        when(imageRepository.save(any(Image.class))).thenReturn(testImage);

        // When
        Image result = updateImageCommandService.updateCaption(IMAGE_ID, nullCaption);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCaption()).isNull();
        verify(imageRepository).findById(IMAGE_ID);
        verify(imageRepository).save(testImage);
    }

    // Alt 텍스트 업데이트 테스트
    @Test
    @DisplayName("TC-009: Alt 텍스트 정상 업데이트")
    void tc009_updateAltText_ValidInput_Success() {
        // Given
        String newAltText = "새로운 대체 텍스트";
        when(imageRepository.findById(IMAGE_ID)).thenReturn(testImage);
        when(imageRepository.save(any(Image.class))).thenReturn(testImage);

        // When
        Image result = updateImageCommandService.updateAltText(IMAGE_ID, newAltText);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAltText()).isEqualTo(newAltText);
        verify(imageRepository).findById(IMAGE_ID);
        verify(imageRepository).save(testImage);
    }

    @Test
    @DisplayName("TC-010: Alt 텍스트 업데이트 NULL imageId - InvalidImageException 발생")
    void tc010_updateAltText_NullImageId_ThrowsException() {
        // Given
        Long nullImageId = null;
        String newAltText = "대체 텍스트";

        // When & Then
        assertThatThrownBy(() -> updateImageCommandService.updateAltText(nullImageId, newAltText))
                .isInstanceOf(InvalidImageException.class);

        verify(imageRepository, never()).findById(any());
    }

    // 이미지 재정렬 테스트
    @Test
    @DisplayName("TC-011: 이미지 재정렬 성공")
    void tc011_reorderImages_ValidIds_Success() {
        // Given
        List<Long> imageIds = Arrays.asList(1L, 2L, 3L);
        List<Image> images = Arrays.asList(
                createImage(1L, "url1", "caption1"),
                createImage(2L, "url2", "caption2"),
                createImage(3L, "url3", "caption3")
        );

        when(imageRepository.findAllById(imageIds)).thenReturn(images);
        when(imageRepository.saveAll(any())).thenReturn(images);

        // When
        var result = updateImageCommandService.reorderImages(imageIds);

        // Then
        assertThat(result).hasSize(3);
        verify(imageRepository).findAllById(imageIds);
        verify(imageRepository).saveAll(any());
    }

    @Test
    @DisplayName("TC-012: NULL 이미지 ID 리스트 - IllegalArgumentException 발생")
    void tc012_reorderImages_NullList_ThrowsException() {
        // Given
        List<Long> nullList = null;

        // When & Then
        assertThatThrownBy(() -> updateImageCommandService.reorderImages(nullList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미지 ID 목록은 필수입니다");

        verify(imageRepository, never()).findAllById(any());
    }

    @Test
    @DisplayName("TC-013: 빈 이미지 ID 리스트 - IllegalArgumentException 발생")
    void tc013_reorderImages_EmptyList_ThrowsException() {
        // Given
        List<Long> emptyList = Collections.emptyList();

        // When & Then
        assertThatThrownBy(() -> updateImageCommandService.reorderImages(emptyList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미지 ID 목록은 필수입니다");

        verify(imageRepository, never()).findAllById(any());
    }

    @Test
    @DisplayName("TC-014: 존재하지 않는 ID 포함 - ImageNotFoundException 발생")
    void tc014_reorderImages_MissingId_ThrowsException() {
        // Given
        List<Long> imageIds = Arrays.asList(1L, 999L);
        List<Image> partialImages = Collections.singletonList(createImage(1L, "url1", "caption1"));

        when(imageRepository.findAllById(imageIds)).thenReturn(partialImages);

        // When & Then
        assertThatThrownBy(() -> updateImageCommandService.reorderImages(imageIds))
                .isInstanceOf(ImageNotFoundException.class);

        verify(imageRepository).findAllById(imageIds);
        verify(imageRepository, never()).saveAll(any());
    }

    // 다중 이미지 업데이트 테스트
    @Test
    @DisplayName("TC-015: 다중 이미지 업데이트 성공")
    void tc015_updateMultipleImages_ValidImages_Success() {
        // Given
        List<Image> images = Arrays.asList(
                createImage(1L, "url1", "caption1"),
                createImage(2L, "url2", "caption2")
        );

        when(imageRepository.existsById(1L)).thenReturn(true);
        when(imageRepository.existsById(2L)).thenReturn(true);
        when(imageRepository.saveAll(images)).thenReturn(images);

        // When
        List<Image> result = updateImageCommandService.updateMultipleImages(images);

        // Then
        assertThat(result).hasSize(2);
        verify(imageRepository, times(2)).existsById(any());
        verify(imageRepository).saveAll(images);
    }

    @Test
    @DisplayName("TC-016: NULL 이미지 리스트 - IllegalArgumentException 발생")
    void tc016_updateMultipleImages_NullList_ThrowsException() {
        // Given
        List<Image> nullList = null;

        // When & Then
        assertThatThrownBy(() -> updateImageCommandService.updateMultipleImages(nullList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미지 목록은 필수입니다");

        verify(imageRepository, never()).existsById(any());
    }

    @Test
    @DisplayName("TC-017: 빈 이미지 리스트 - IllegalArgumentException 발생")
    void tc017_updateMultipleImages_EmptyList_ThrowsException() {
        // Given
        List<Image> emptyList = Collections.emptyList();

        // When & Then
        assertThatThrownBy(() -> updateImageCommandService.updateMultipleImages(emptyList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미지 목록은 필수입니다");

        verify(imageRepository, never()).existsById(any());
    }

    @Test
    @DisplayName("TC-018: 존재하지 않는 이미지 포함 - ImageNotFoundException 발생")
    void tc018_updateMultipleImages_NonExistentImage_ThrowsException() {
        // Given
        List<Image> images = Arrays.asList(
                createImage(1L, "url1", "caption1"),
                createImage(999L, "url999", "caption999")
        );

        when(imageRepository.existsById(1L)).thenReturn(true);
        when(imageRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> updateImageCommandService.updateMultipleImages(images))
                .isInstanceOf(ImageNotFoundException.class);

        verify(imageRepository, times(2)).existsById(any());
        verify(imageRepository, never()).saveAll(any());
    }

    // 헬퍼 메소드
    private Image createImage(Long id, String imageUrl, String caption) {
        return Image.builder()
                .id(id)
                .imageUrl(imageUrl)
                .caption(caption)
                .mimeType("image/jpeg")
                .build();
    }
}