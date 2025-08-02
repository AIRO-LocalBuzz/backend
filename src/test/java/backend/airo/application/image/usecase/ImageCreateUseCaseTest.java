//package backend.airo.application.image.usecase;
//
//import backend.airo.domain.image.Image;
//import backend.airo.domain.image.command.CreateImageCommandService;
//import backend.airo.domain.image.command.DeleteImageCommandService;
//import backend.airo.domain.image.command.UpdateImageCommandService;
//import backend.airo.domain.image.query.GetImageQueryService;
//import backend.airo.domain.image.repository.ImageRepository;
//import backend.airo.domain.image.exception.ImageNotFoundException;
//import backend.airo.domain.image.exception.UnsupportedFormatException;
//import backend.airo.domain.image.exception.UnauthorizedException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ImageCreateUseCaseTest {
//
//    @Mock
//    private ImageRepository imageRepository;
//
//    private CreateImageCommandService createImageCommandService;
//    private GetImageQueryService getImageQueryService;
//    private UpdateImageCommandService updateImageCommandService;
//    private DeleteImageCommandService deleteImageCommandService;
//
//    @BeforeEach
//    void setUp() {
//        createImageCommandService = new CreateImageCommandService(imageRepository);
//        getImageQueryService = new GetImageQueryService(imageRepository);
//        updateImageCommandService = new UpdateImageCommandService(imageRepository);
//        deleteImageCommandService = new DeleteImageCommandService(imageRepository);
//    }
//
//    // ========== 이미지 업로드 (Create) ==========
//
//    @Test
//    @DisplayName("TC-001: 업로드_성공_단일이미지")
//    void uploadSingleImageSuccess() {
//        // given
//        Image image = createValidImage();
//        when(imageRepository.save(any(Image.class))).thenReturn(image);
//
//        // when
//        Image result = createImageCommandService.handle(image);
//
//        // then
//        assertThat(result).isNotNull();
//        assertThat(result.getImageUrl()).isEqualTo("https://example.com/image.jpg");
//        verify(imageRepository, times(1)).save(image);
//    }
//
//    @Test
//    @DisplayName("TC-002: 업로드_성공_다중이미지")
//    void uploadMultipleImagesSuccess() {
//        // given
//        List<Image> images = Arrays.asList(
//                createValidImage("image1.jpg"),
//                createValidImage("image2.jpg"),
//                createValidImage("image3.jpg")
//        );
//
//        when(imageRepository.save(any(Image.class)))
//                .thenReturn(images.get(0))
//                .thenReturn(images.get(1))
//                .thenReturn(images.get(2));
//
//        // when
//        List<Image> results = images.stream()
//                .map(createImageCommandService::handle)
//                .toList();
//
//        // then
//        assertThat(results).hasSize(3);
//        verify(imageRepository, times(3)).save(any(Image.class));
//    }
//
//    @Test
//    @DisplayName("TC-003: 업로드_실패_정보없음")
//    void uploadFailWhenImageIsNull() {
//        // when & then
//        assertThatThrownBy(() -> createImageCommandService.handle(null))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("이미지 정보는 필수입니다");
//
//        verify(imageRepository, never()).save(any());
//    }
//
//    @Test
//    @DisplayName("TC-004: 업로드_실패_지원하지않는형식")
//    void uploadFailWhenUnsupportedFormat() {
//        // given
//        Image image = Image.builder()
//                .imageUrl("https://example.com/file.pdf")
//                .mimeType("application/pdf")
//                .build();
//
//        // when & then
//        assertThatThrownBy(() -> createImageCommandService.handle(image))
//                .isInstanceOf(UnsupportedFormatException.class)
//                .hasMessage("지원하지 않는 파일 형식입니다: application/pdf. 지원되는 형식: image/jpeg, image/jpg, image/png, image/gif");
//
//        verify(imageRepository, never()).save(any());
//    }
//
//    // ========== 이미지 조회 (Read) ==========
//
//    @Test
//    @DisplayName("TC-005: 단일이미지조회_성공")
//    void getSingleImageSuccess() {
//        // given
//        Long imageId = 1L;
//        Image image = createValidImage();
//        when(imageRepository.findById(imageId)).thenReturn(image);
//
//        // when
//        Image result = getImageQueryService.getSingleImage(imageId);
//
//        // then
//        assertThat(result).isNotNull();
//        assertThat(result.getImageUrl()).isEqualTo("https://example.com/image.jpg");
//        verify(imageRepository, times(1)).findById(imageId);
//    }
//
//    @Test
//    @DisplayName("TC-006: 단일이미지조회_실패_존재하지않음")
//    void getSingleImageFailWhenNotExists() {
//        // given
//        Long imageId = 999L;
//        when(imageRepository.findById(imageId))
//                .thenThrow(new ImageNotFoundException(imageId));
//
//        // when & then
//        assertThatThrownBy(() -> getImageQueryService.getSingleImage(imageId))
//                .isInstanceOf(ImageNotFoundException.class)
//                .hasMessage("이미지를 찾을 수 없습니다: " + imageId);
//
//        verify(imageRepository, times(1)).findById(imageId);
//    }
//
//    @Test
//    @DisplayName("TC-007: 게시물별이미지목록조회_성공")
//    void getImagesByPostSuccess() {
//        // given
//        Long postId = 1L;
//        List<Image> images = Arrays.asList(
//                createValidImage("image1.jpg"),
//                createValidImage("image2.jpg")
//        );
//        when(imageRepository.findImagesAllByPostId(postId)).thenReturn(images);
//
//        // when
//        List<Image> result = (List<Image>) getImageQueryService.getImagesBelongsPost(postId);
//
//        // then
//        assertThat(result).hasSize(2);
//        verify(imageRepository, times(1)).findImagesAllByPostId(postId);
//    }
//
//    @Test
//    @DisplayName("TC-008: 게시물별이미지목록조회_성공_빈목록")
//    void getImagesByPostSuccessEmptyList() {
//        // given
//        Long postId = 2L;
//        when(imageRepository.findImagesAllByPostId(postId)).thenReturn(Collections.emptyList());
//
//        // when
//        List<Image> result = (List<Image>) getImageQueryService.getImagesBelongsPost(postId);
//
//        // then
//        assertThat(result).isEmpty();
//        verify(imageRepository, times(1)).findImagesAllByPostId(postId);
//    }
//
//    @Test
//    @DisplayName("TC-009: 페이징된이미지목록조회_성공")
//    void getPagedImagesSuccess() {
//        // given
//        Pageable pageable = PageRequest.of(0, 10);
//        List<Image> images = Arrays.asList(createValidImage(), createValidImage());
//        Page<Image> imagePage = new PageImpl<>(images, pageable, 2);
//
//        when(imageRepository.findAll(pageable)).thenReturn(imagePage);
//
//        // when
//        Page<Image> result = getImageQueryService.getPagedImages(pageable);
//
//        // then
//        assertThat(result.getContent()).hasSize(2);
//        assertThat(result.getTotalElements()).isEqualTo(2);
//        verify(imageRepository, times(1)).findAll(pageable);
//    }
//
//    @Test
//    @DisplayName("TC-010: 정렬된이미지목록조회_성공_정렬순서별")
//    void getSortedImagesSuccess() {
//        // given
//        Long postId = 1L;
//        List<Image> sortedImages = Arrays.asList(
//                createImageWithSortOrder(1),
//                createImageWithSortOrder(2),
//                createImageWithSortOrder(3)
//        );
//        when(imageRepository.findByPostIdOrderBySortOrder(postId)).thenReturn(sortedImages);
//
//        // when
//        List<Image> result = getImageQueryService.getSortedImagesByPost(postId);
//
//        // then
//        assertThat(result).hasSize(3);
//        assertThat(result.get(0).getSortOrder()).isEqualTo(1);
//        assertThat(result.get(1).getSortOrder()).isEqualTo(2);
//        assertThat(result.get(2).getSortOrder()).isEqualTo(3);
//        verify(imageRepository, times(1)).findByPostIdOrderBySortOrder(postId);
//    }
//
//    // ========== 이미지 수정 (Update) ==========
//
//    @Test
//    @DisplayName("TC-011: 이미지순서변경_성공")
//    void updateImageOrderSuccess() {
//        // given
//        Long imageId = 1L;
//        Integer newOrder = 5;
//        Image existingImage = createValidImage();
//        Image updatedImage = createImageWithSortOrder(newOrder);
//
//        when(imageRepository.findById(imageId)).thenReturn(existingImage);
//        when(imageRepository.save(any(Image.class))).thenReturn(updatedImage);
//
//        // when
//        Image result = updateImageCommandService.updateSortOrder(imageId, newOrder);
//
//        // then
//        assertThat(result.getSortOrder()).isEqualTo(newOrder);
//        verify(imageRepository, times(1)).findById(imageId);
//        verify(imageRepository, times(1)).save(any(Image.class));
//    }
//
//    @Test
//    @DisplayName("TC-012: 이미지캡션수정_성공")
//    void updateImageCaptionSuccess() {
//        // given
//        Long imageId = 1L;
//        String newCaption = "새로운 캡션";
//        Image existingImage = createValidImage();
//        Image updatedImage = createImageWithCaption(newCaption);
//
//        when(imageRepository.findById(imageId)).thenReturn(existingImage);
//        when(imageRepository.save(any(Image.class))).thenReturn(updatedImage);
//
//        // when
//        Image result = updateImageCommandService.updateCaption(imageId, newCaption);
//
//        // then
//        assertThat(result.getCaption()).isEqualTo(newCaption);
//        verify(imageRepository, times(1)).findById(imageId);
//        verify(imageRepository, times(1)).save(any(Image.class));
//    }
//
//    // ========== 이미지 삭제 (Delete) ==========
//
//    @Test
//    @DisplayName("TC-014: 단일이미지삭제_성공")
//    void deleteSingleImageSuccess() {
//        // given
//        Long imageId = 1L;
//        Image existingImage = createValidImage();
//        when(imageRepository.findById(imageId)).thenReturn(existingImage);
//        doNothing().when(imageRepository).deleteById(imageId);
//
//        // when
//        deleteImageCommandService.deleteById(imageId);
//
//        // then
//        verify(imageRepository, times(1)).findById(imageId);
//        verify(imageRepository, times(1)).deleteById(imageId);
//    }
//
//
//    @Test
//    @DisplayName("TC-015: 다중이미지삭제_성공")
//    void deleteMultipleImagesSuccess() {
//        // given
//        List<Long> imageIds = Arrays.asList(1L, 2L, 3L);
//
//        // existsById mock 설정
//        when(imageRepository.existsById(1L)).thenReturn(true);
//        when(imageRepository.existsById(2L)).thenReturn(true);
//        when(imageRepository.existsById(3L)).thenReturn(true);
//
//        doNothing().when(imageRepository).deleteAllById(imageIds);
//
//        // when
//        deleteImageCommandService.deleteAllById(imageIds);
//
//        // then
//        verify(imageRepository, times(3)).existsById(anyLong());
//        verify(imageRepository, times(1)).deleteAllById(imageIds);
//    }
//
//    @Test
//    @DisplayName("TC-016: 이미지삭제_성공")
//    void deleteImageSuccess() {
//        // given
//        Long imageId = 1L;
//        Image existingImage = createValidImage();
//        when(imageRepository.findById(imageId)).thenReturn(existingImage);
//        doNothing().when(imageRepository).deleteById(imageId);
//
//        // when
//        boolean result = deleteImageCommandService.deleteById(imageId);
//
//        // then
//        assertThat(result).isTrue();
//        verify(imageRepository, times(1)).findById(imageId);
//        verify(imageRepository, times(1)).deleteById(imageId);
//    }
//
//    @Test
//    @DisplayName("TC-017: 이미지삭제_실패_존재하지않음")
//    void deleteImageFailWhenNotExists() {
//        // given
//        Long imageId = 999L;
//        when(imageRepository.findById(imageId)).thenReturn(null);
//
//        // when & then
//        assertThatThrownBy(() -> deleteImageCommandService.deleteById(imageId))
//                .isInstanceOf(ImageNotFoundException.class)
//                .hasMessage("이미지를 찾을 수 없습니다: " + imageId);
//
//        verify(imageRepository, times(1)).findById(imageId);
//        verify(imageRepository, never()).deleteById(any());
//    }
//
//    @Test
//    @DisplayName("TC-018: 이미지삭제_실패_권한없음")
//    void deleteImageFailWhenUnauthorized() {
//        // given
//        Long imageId = 1L;
//        Long currentUserId = 2L;
//        Image existingImage = createImageWithUserId(1L); // 다른 사용자의 이미지
//
//        when(imageRepository.findById(imageId)).thenReturn(existingImage);
//
//        // when & then
//        assertThatThrownBy(() -> deleteImageCommandService.deleteById(imageId, currentUserId))
//                .isInstanceOf(UnauthorizedException.class)
//                .hasMessage("이미지 삭제 권한이 없습니다");
//
//        verify(imageRepository, times(1)).findById(imageId);
//        verify(imageRepository, never()).deleteById(any());
//    }
//
//
//
//    @Test
//    @DisplayName("TC-019: 게시물삭제시_연관이미지모두삭제")
//    void deleteImagesByPostSuccess() {
//        // given
//        Long postId = 1L;
//        doNothing().when(imageRepository).deleteByPostId(postId);
//
//        // when
//        deleteImageCommandService.deleteByPostId(postId);
//
//        // then
//        verify(imageRepository, times(1)).deleteByPostId(postId);
//    }
//
//    // ========== 비즈니스 로직 ==========
//
//    @Test
//    @DisplayName("TC-020: 이미지순서재정렬_성공")
//    void reorderImagesSuccess() {
//        // given
//        List<Long> imageIds = Arrays.asList(3L, 1L, 2L);
//        List<Image> images = Arrays.asList(
//                createImageWithSortOrder(1),
//                createImageWithSortOrder(2),
//                createImageWithSortOrder(3)
//        );
//
//        when(imageRepository.findAllById(imageIds)).thenReturn(images);
//        when(imageRepository.saveAll(anyList())).thenReturn(images);
//
//        // when
//        Collection<Image> result = updateImageCommandService.reorderImages(imageIds);
//
//        // then
//        assertThat(result).hasSize(3);
//        verify(imageRepository, times(1)).findAllById(imageIds);
//        verify(imageRepository, times(1)).saveAll(anyList());
//    }
//
//
//
//    // ========== 예외 상황 ==========
//
//    @Test
//    @DisplayName("TC-022: 네트워크오류시_재시도로직")
//    void retryOnNetworkError() {
//        // given
//        Image image = createValidImage();
//
//        // 처음 두 번은 실패, 세 번째는 성공
//        when(imageRepository.save(any(Image.class)))
//                .thenThrow(new RuntimeException("Network error"))
//                .thenThrow(new RuntimeException("Network error"))
//                .thenReturn(image);
//
//        // when
//        Image result = createImageCommandService.handleWithRetry(image);
//
//        // then
//        assertThat(result).isNotNull();
//        assertThat(result.getImageUrl()).isEqualTo("https://example.com/image.jpg");
//        verify(imageRepository, times(3)).save(any(Image.class));
//    }
//
//    @Test
//    @DisplayName("TC-023: 동시업로드시_동시성제어")
//    void concurrentUploadControl() {
//        // given
//        Image image = createValidImage();
//        when(imageRepository.save(any(Image.class))).thenReturn(image);
//
//        // when
//        Image result = createImageCommandService.handleWithLock(image);
//
//        // then
//        assertThat(result).isNotNull();
//        verify(imageRepository, times(1)).save(image);
//    }
//
//    // ========== Helper Methods ==========
//
//    private Image createValidImage() {
//        return Image.builder()
//                .id(1L) // ID 추가
//                .imageUrl("https://example.com/image.jpg")
//                .mimeType("image/jpeg")
//                .fileSize(1024L)
//                .width(800)
//                .height(600)
//                .sortOrder(1)
//                .isCover(false)
//                .build();
//    }
//
//    private Image createValidImage(String filename) {
//        return Image.builder()
//                .imageUrl("https://example.com/" + filename)
//                .mimeType("image/jpeg")
//                .fileSize(1024L)
//                .width(800)
//                .height(600)
//                .sortOrder(1)
//                .isCover(false)
//                .build();
//    }
//
//    private Image createImageWithSortOrder(Integer sortOrder) {
//        return Image.builder()
//                .id(sortOrder.longValue()) // ID 추가
//                .imageUrl("https://example.com/image.jpg")
//                .mimeType("image/jpeg")
//                .sortOrder(sortOrder)
//                .build();
//    }
//
//    private Image createImageWithCaption(String caption) {
//        return Image.builder()
//                .imageUrl("https://example.com/image.jpg")
//                .caption(caption)
//                .build();
//    }
//
//
//    private Image createImageWithUserId(Long userId) {
//        return Image.builder()
//                .id(1L)
//                .imageUrl("https://example.com/image.jpg")
//                .userId(userId)
//                .build();
//    }
//
//}