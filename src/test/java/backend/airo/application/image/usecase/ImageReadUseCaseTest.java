package backend.airo.application.image.usecase;

import backend.airo.domain.image.Image;
import backend.airo.domain.image.exception.ImageNotFoundException;
import backend.airo.domain.image.query.GetImageQueryService;
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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ImageReadUseCase 테스트")
class ImageReadUseCaseTest {

    @Mock
    private GetImageQueryService getImageQueryService;

    @InjectMocks
    private ImageReadUseCase imageReadUseCase;

    private static final Long VALID_IMAGE_ID = 1L;
    private static final Long INVALID_IMAGE_ID = 999L;
    private static final Long VALID_POST_ID = 10L;
    private static final Long POST_ID_WITHOUT_IMAGES = 999L;

    private Image testImage;
    private List<Image> testImages;
    private Page<Image> testImagePage;

    @BeforeEach
    void setUp() {
        testImage = createTestImage(VALID_IMAGE_ID, "https://example.com/image1.jpg");
        testImages = Arrays.asList(
                createTestImage(1L, "https://example.com/image1.jpg"),
                createTestImage(2L, "https://example.com/image2.jpg"),
                createTestImage(3L, "https://example.com/image3.jpg")
        );

        Pageable pageable = PageRequest.of(0, 10);
        testImagePage = new PageImpl<>(testImages, pageable, testImages.size());
    }

    // 단일 이미지 조회 테스트
    @Test
    @DisplayName("TC-001: 단일 이미지 조회 성공")
    void tc001_getSingleImage_ValidImageId_Success() {
        // Given
        when(getImageQueryService.getSingleImage(VALID_IMAGE_ID)).thenReturn(testImage);

        // When
        Image result = imageReadUseCase.getSingleImage(VALID_IMAGE_ID);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(VALID_IMAGE_ID);
        assertThat(result.getImageUrl()).isEqualTo("https://example.com/image1.jpg");
        verify(getImageQueryService, times(1)).getSingleImage(VALID_IMAGE_ID);
    }

    @Test
    @DisplayName("TC-002: 단일 이미지 조회 실패 - 존재하지 않는 이미지")
    void tc002_getSingleImage_NonExistentImageId_ThrowsException() {
        // Given
        when(getImageQueryService.getSingleImage(INVALID_IMAGE_ID))
                .thenThrow(new ImageNotFoundException(INVALID_IMAGE_ID));

        // When & Then
        assertThatThrownBy(() -> imageReadUseCase.getSingleImage(INVALID_IMAGE_ID))
                .isInstanceOf(ImageNotFoundException.class);

        verify(getImageQueryService, times(1)).getSingleImage(INVALID_IMAGE_ID);
    }

    // 페이징 이미지 목록 조회 테스트
    @Test
    @DisplayName("TC-003: 페이징 이미지 목록 조회 성공")
    void tc003_getPagedImages_ValidPageable_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        when(getImageQueryService.getPagedImages(pageable)).thenReturn(testImagePage);

        // When
        Page<Image> result = imageReadUseCase.getPagedImages(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(10);
        verify(getImageQueryService, times(1)).getPagedImages(pageable);
    }

    @Test
    @DisplayName("TC-004: 페이징 이미지 목록 조회 - 빈 결과")
    void tc004_getPagedImages_EmptyResult_Success() {
        // Given
        Pageable pageable = PageRequest.of(100, 10);
        Page<Image> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(getImageQueryService.getPagedImages(pageable)).thenReturn(emptyPage);

        // When
        Page<Image> result = imageReadUseCase.getPagedImages(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getNumber()).isEqualTo(100);
        verify(getImageQueryService, times(1)).getPagedImages(pageable);
    }

    @Test
    @DisplayName("TC-007: 페이징 조회 - 첫 번째 페이지")
    void tc007_getPagedImages_FirstPage_Success() {
        // Given
        Pageable firstPageable = PageRequest.of(0, 5);
        List<Image> firstPageImages = testImages.subList(0, 2);
        Page<Image> firstPage = new PageImpl<>(firstPageImages, firstPageable, testImages.size());
        when(getImageQueryService.getPagedImages(firstPageable)).thenReturn(firstPage);

        // When
        Page<Image> result = imageReadUseCase.getPagedImages(firstPageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.isFirst()).isTrue();
        verify(getImageQueryService, times(1)).getPagedImages(firstPageable);
    }

    @Test
    @DisplayName("TC-008: 페이징 조회 - 큰 페이지 크기")
    void tc008_getPagedImages_LargePageSize_Success() {
        // Given
        Pageable largePageable = PageRequest.of(0, 100);
        Page<Image> largePage = new PageImpl<>(testImages, largePageable, testImages.size());
        when(getImageQueryService.getPagedImages(largePageable)).thenReturn(largePage);

        // When
        Page<Image> result = imageReadUseCase.getPagedImages(largePageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getSize()).isEqualTo(100);
        assertThat(result.getTotalElements()).isEqualTo(3);
        verify(getImageQueryService, times(1)).getPagedImages(largePageable);
    }

    // 게시물별 정렬된 이미지 조회 테스트
    @Test
    @DisplayName("TC-005: 게시물별 정렬된 이미지 조회 성공")
    void tc005_getSortedImagesByPost_ValidPostId_Success() {
        // Given
        when(getImageQueryService.getSortedImagesByPost(VALID_POST_ID)).thenReturn(testImages);

        // When
        List<Image> result = imageReadUseCase.getSortedImagesByPost(VALID_POST_ID);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(testImages);
        verify(getImageQueryService, times(1)).getSortedImagesByPost(VALID_POST_ID);
    }

    @Test
    @DisplayName("TC-006: 게시물별 이미지 조회 - 이미지 없는 게시물")
    void tc006_getSortedImagesByPost_PostWithoutImages_ReturnsEmptyList() {
        // Given
        when(getImageQueryService.getSortedImagesByPost(POST_ID_WITHOUT_IMAGES))
                .thenReturn(Collections.emptyList());

        // When
        List<Image> result = imageReadUseCase.getSortedImagesByPost(POST_ID_WITHOUT_IMAGES);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(getImageQueryService, times(1)).getSortedImagesByPost(POST_ID_WITHOUT_IMAGES);
    }

    // 통합 테스트
    @Test
    @DisplayName("TC-009: 모든 조회 메서드 호출 검증")
    void tc009_allReadMethods_CallCorrectQueryServiceMethods() {
        // Given
        Long imageId = 1L;
        Long postId = 10L;
        Pageable pageable = PageRequest.of(0, 10);

        when(getImageQueryService.getSingleImage(imageId)).thenReturn(testImage);
        when(getImageQueryService.getPagedImages(pageable)).thenReturn(testImagePage);
        when(getImageQueryService.getSortedImagesByPost(postId)).thenReturn(testImages);

        // When
        Image singleResult = imageReadUseCase.getSingleImage(imageId);
        Page<Image> pagedResult = imageReadUseCase.getPagedImages(pageable);
        List<Image> sortedResult = imageReadUseCase.getSortedImagesByPost(postId);

        // Then
        assertThat(singleResult).isNotNull();
        assertThat(pagedResult).isNotNull();
        assertThat(sortedResult).isNotNull();

        verify(getImageQueryService, times(1)).getSingleImage(imageId);
        verify(getImageQueryService, times(1)).getPagedImages(pageable);
        verify(getImageQueryService, times(1)).getSortedImagesByPost(postId);

        // 각 메서드가 정확히 한 번씩만 호출되었는지 확인
        verifyNoMoreInteractions(getImageQueryService);
    }

    // 경계값 테스트
    @Test
    @DisplayName("TC-010: null 입력값 처리")
    void tc010_nullInputs_HandledByQueryService() {
        // Given
        when(getImageQueryService.getSingleImage(null))
                .thenThrow(new IllegalArgumentException("Image ID cannot be null"));

        // When & Then
        assertThatThrownBy(() -> imageReadUseCase.getSingleImage(null))
                .isInstanceOf(IllegalArgumentException.class);

        verify(getImageQueryService, times(1)).getSingleImage(null);
    }

    @Test
    @DisplayName("TC-011: 다양한 페이지 크기 처리")
    void tc011_variousPageSizes_AllHandledCorrectly() {
        // Given
        Pageable smallPage = PageRequest.of(0, 1);
        Pageable mediumPage = PageRequest.of(0, 10);
        Pageable largePage = PageRequest.of(0, 50);

        Page<Image> smallResult = new PageImpl<>(testImages.subList(0, 1), smallPage, testImages.size());
        Page<Image> mediumResult = new PageImpl<>(testImages, mediumPage, testImages.size());
        Page<Image> largeResult = new PageImpl<>(testImages, largePage, testImages.size());

        when(getImageQueryService.getPagedImages(smallPage)).thenReturn(smallResult);
        when(getImageQueryService.getPagedImages(mediumPage)).thenReturn(mediumResult);
        when(getImageQueryService.getPagedImages(largePage)).thenReturn(largeResult);

        // When
        Page<Image> small = imageReadUseCase.getPagedImages(smallPage);
        Page<Image> medium = imageReadUseCase.getPagedImages(mediumPage);
        Page<Image> large = imageReadUseCase.getPagedImages(largePage);

        // Then
        assertThat(small.getSize()).isEqualTo(1);
        assertThat(medium.getSize()).isEqualTo(10);
        assertThat(large.getSize()).isEqualTo(50);

        verify(getImageQueryService, times(1)).getPagedImages(smallPage);
        verify(getImageQueryService, times(1)).getPagedImages(mediumPage);
        verify(getImageQueryService, times(1)).getPagedImages(largePage);
    }

    // 헬퍼 메서드
    private Image createTestImage(Long id, String imageUrl) {
        return Image.builder()
                .id(id)
                .postId(1L)
                .imageUrl(imageUrl)
                .mimeType("image/jpeg")
                .build();
    }
}