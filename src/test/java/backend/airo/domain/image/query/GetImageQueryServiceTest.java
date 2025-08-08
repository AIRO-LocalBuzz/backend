package backend.airo.domain.image.query;

import backend.airo.domain.image.Image;
import backend.airo.domain.image.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetImageQueryService 테스트")
class GetImageQueryServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private GetImageQueryService getImageQueryService;

    private Image testImage;
    private final Long IMAGE_ID = 1L;
    private final Long POST_ID = 100L;

    @BeforeEach
    void setUp() {
        testImage = createImage(IMAGE_ID, "https://example.com/image.jpg");
    }

    // 단일 이미지 조회 테스트
    @Test
    @DisplayName("TC-001: 단일 이미지 조회 성공")
    void tc001_getSingleImage_ValidId_Success() {
        // Given
        when(imageRepository.existsById(IMAGE_ID)).thenReturn(true);
        when(imageRepository.findById(IMAGE_ID)).thenReturn(testImage);

        // When
        Image result = getImageQueryService.getSingleImage(IMAGE_ID);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(IMAGE_ID);
        verify(imageRepository).existsById(IMAGE_ID);
        verify(imageRepository).findById(IMAGE_ID);
    }

    @Test
    @DisplayName("TC-002: NULL ID - IllegalArgumentException 발생")
    void tc002_getSingleImage_NullId_ThrowsException() {
        // Given
        Long nullId = null;

        // When & Then
        assertThatThrownBy(() -> getImageQueryService.getSingleImage(nullId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지 ID는 필수입니다");

        verify(imageRepository, never()).existsById(any());
        verify(imageRepository, never()).findById(any());
    }

    @Test
    @DisplayName("TC-003: 존재하지 않는 ID - IllegalArgumentException 발생")
    void tc003_getSingleImage_NonExistentId_ThrowsException() {
        // Given
        Long nonExistentId = 999L;
        when(imageRepository.existsById(nonExistentId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> getImageQueryService.getSingleImage(nonExistentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 ID의 이미지를 찾을 수 없습니다");

        verify(imageRepository).existsById(nonExistentId);
        verify(imageRepository, never()).findById(any());
    }

    // 이미지 존재 확인 테스트
    @Test
    @DisplayName("TC-004: 이미지 존재 확인 - 존재함")
    void tc004_imageExists_ExistingImage_ReturnsTrue() {
        // Given
        when(imageRepository.existsById(IMAGE_ID)).thenReturn(true);

        // When
        boolean result = getImageQueryService.imageExists(IMAGE_ID);

        // Then
        assertThat(result).isTrue();
        verify(imageRepository).existsById(IMAGE_ID);
    }

    @Test
    @DisplayName("TC-005: 이미지 존재 확인 - 존재하지 않음")
    void tc005_imageExists_NonExistentImage_ReturnsFalse() {
        // Given
        Long nonExistentId = 999L;
        when(imageRepository.existsById(nonExistentId)).thenReturn(false);

        // When
        boolean result = getImageQueryService.imageExists(nonExistentId);

        // Then
        assertThat(result).isFalse();
        verify(imageRepository).existsById(nonExistentId);
    }

    @Test
    @DisplayName("TC-006: 이미지 존재 확인 NULL ID - IllegalArgumentException 발생")
    void tc006_imageExists_NullId_ThrowsException() {
        // Given
        Long nullId = null;

        // When & Then
        assertThatThrownBy(() -> getImageQueryService.imageExists(nullId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지 ID는 필수입니다");

        verify(imageRepository, never()).existsById(any());
    }

    // 게시물별 이미지 조회 테스트
    @Test
    @DisplayName("TC-007: 게시물별 이미지 조회 성공")
    void tc007_getImagesBelongsPost_ValidPostId_Success() {
        // Given
        List<Image> postImages = Arrays.asList(testImage, createImage(2L, "https://example.com/image2.jpg"));
        when(imageRepository.findImagesAllByPostId(POST_ID)).thenReturn(postImages);

        // When
        Collection<Image> result = getImageQueryService.getImagesBelongsPost(POST_ID);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(postImages);
        verify(imageRepository).findImagesAllByPostId(POST_ID);
    }

    @Test
    @DisplayName("TC-008: 게시물별 이미지 조회 NULL postId - IllegalArgumentException 발생")
    void tc008_getImagesBelongsPost_NullPostId_ThrowsException() {
        // Given
        Long nullPostId = null;

        // When & Then
        assertThatThrownBy(() -> getImageQueryService.getImagesBelongsPost(nullPostId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시물 ID는 필수입니다");

        verify(imageRepository, never()).findImagesAllByPostId(any());
    }

    // 페이지네이션 조회 테스트
    @Test
    @DisplayName("TC-009: 페이지네이션 조회 성공")
    void tc009_getPagedImages_ValidPageable_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Image> images = Arrays.asList(testImage, createImage(2L, "https://example.com/image2.jpg"));
        Page<Image> page = new PageImpl<>(images, pageable, images.size());
        when(imageRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<Image> result = getImageQueryService.getPagedImages(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(imageRepository).findAll(pageable);
    }

    @Test
    @DisplayName("TC-010: 페이지네이션 조회 NULL Pageable - IllegalArgumentException 발생")
    void tc010_getPagedImages_NullPageable_ThrowsException() {
        // Given
        Pageable nullPageable = null;

        // When & Then
        assertThatThrownBy(() -> getImageQueryService.getPagedImages(nullPageable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("페이지 정보는 필수입니다");

        verify(imageRepository, never()).findAll(any(Pageable.class));
    }

    // 정렬된 이미지 조회 테스트
    @Test
    @DisplayName("TC-011: 정렬된 이미지 조회 성공")
    void tc011_getSortedImagesByPost_ValidPostId_Success() {
        // Given
        List<Image> sortedImages = Arrays.asList(
                createImageWithSortOrder(1L, 1),
                createImageWithSortOrder(2L, 2),
                createImageWithSortOrder(3L, 3)
        );
        when(imageRepository.findByPostIdOrderBySortOrder(POST_ID)).thenReturn(sortedImages);

        // When
        List<Image> result = getImageQueryService.getSortedImagesByPost(POST_ID);

        // Then
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(sortedImages);
        verify(imageRepository).findByPostIdOrderBySortOrder(POST_ID);
    }

    @Test
    @DisplayName("TC-012: 정렬된 이미지 조회 NULL postId - IllegalArgumentException 발생")
    void tc012_getSortedImagesByPost_NullPostId_ThrowsException() {
        // Given
        Long nullPostId = null;

        // When & Then
        assertThatThrownBy(() -> getImageQueryService.getSortedImagesByPost(nullPostId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시물 ID는 필수입니다");

        verify(imageRepository, never()).findByPostIdOrderBySortOrder(any());
    }

    // MIME 타입별 조회 테스트
    @Test
    @DisplayName("TC-013: MIME 타입별 조회 성공")
    void tc013_getImagesByMimeType_ValidMimeType_Success() {
        // Given
        String mimeType = "image/jpeg";
        List<Image> jpegImages = Arrays.asList(testImage, createImage(2L, "https://example.com/image2.jpg"));
        when(imageRepository.findByMimeType(mimeType)).thenReturn(jpegImages);

        // When
        List<Image> result = getImageQueryService.getImagesByMimeType(mimeType);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(jpegImages);
        verify(imageRepository).findByMimeType(mimeType);
    }

    @Test
    @DisplayName("TC-014: MIME 타입별 조회 NULL MIME 타입 - IllegalArgumentException 발생")
    void tc014_getImagesByMimeType_NullMimeType_ThrowsException() {
        // Given
        String nullMimeType = null;

        // When & Then
        assertThatThrownBy(() -> getImageQueryService.getImagesByMimeType(nullMimeType))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("MIME 타입은 필수입니다");

        verify(imageRepository, never()).findByMimeType(any());
    }

    @Test
    @DisplayName("TC-015: MIME 타입별 조회 빈 MIME 타입 - IllegalArgumentException 발생")
    void tc015_getImagesByMimeType_EmptyMimeType_ThrowsException() {
        // Given
        String emptyMimeType = "";

        // When & Then
        assertThatThrownBy(() -> getImageQueryService.getImagesByMimeType(emptyMimeType))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("MIME 타입은 필수입니다");

        verify(imageRepository, never()).findByMimeType(any());
    }

    @Test
    @DisplayName("TC-016: MIME 타입별 조회 공백 MIME 타입 - IllegalArgumentException 발생")
    void tc016_getImagesByMimeType_BlankMimeType_ThrowsException() {
        // Given
        String blankMimeType = "   ";

        // When & Then
        assertThatThrownBy(() -> getImageQueryService.getImagesByMimeType(blankMimeType))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("MIME 타입은 필수입니다");

        verify(imageRepository, never()).findByMimeType(any());
    }

    @Test
    @DisplayName("TC-017: MIME 타입별 조회 결과 없음")
    void tc017_getImagesByMimeType_NoResults_ReturnsEmptyList() {
        // Given
        String mimeType = "image/gif";
        when(imageRepository.findByMimeType(mimeType)).thenReturn(Collections.emptyList());

        // When
        List<Image> result = getImageQueryService.getImagesByMimeType(mimeType);

        // Then
        assertThat(result).isEmpty();
        verify(imageRepository).findByMimeType(mimeType);
    }

    @Test
    @DisplayName("TC-018: 게시물별 이미지 조회 결과 없음")
    void tc018_getImagesBelongsPost_NoResults_ReturnsEmptyCollection() {
        // Given
        when(imageRepository.findImagesAllByPostId(POST_ID)).thenReturn(Collections.emptyList());

        // When
        Collection<Image> result = getImageQueryService.getImagesBelongsPost(POST_ID);

        // Then
        assertThat(result).isEmpty();
        verify(imageRepository).findImagesAllByPostId(POST_ID);
    }

    // 헬퍼 메소드
    private Image createImage(Long id, String imageUrl) {
        return Image.builder()
                .id(id)
                .imageUrl(imageUrl)
                .mimeType("image/jpeg")
                .build();
    }

    private Image createImageWithSortOrder(Long id, Integer sortOrder) {
        Long postId = 100L; // 예시 게시물 ID
        String imageUrl = "https://example.com/image2.jpg";
        String mimeType = "image/jpeg";

        return new Image(id, postId, imageUrl, mimeType, sortOrder);

    }
}
