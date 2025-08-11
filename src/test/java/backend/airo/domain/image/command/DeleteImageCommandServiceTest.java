//package backend.airo.domain.image.command;
//
//import backend.airo.domain.image.Image;
//import backend.airo.domain.image.exception.ImageNotFoundException;
//import backend.airo.domain.image.exception.UnauthorizedException;
//import backend.airo.domain.image.repository.ImageRepository;
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
//@DisplayName("DeleteImageCommandService 테스트")
//class DeleteImageCommandServiceTest {
//
//    @Mock
//    private ImageRepository imageRepository;
//
//    @InjectMocks
//    private DeleteImageCommandService deleteImageCommandService;
//
//    private Image ownedImage;
//    private final Long OWNER_USER_ID = 1L;
//    private final Long OTHER_USER_ID = 2L;
//    private final Long IMAGE_ID = 100L;
//    private final Long POST_ID = 200L;
//    private List<Image> testImages;
//    private List<Long> testImageIds;
//
//    @BeforeEach
//    void setUp() {
//        ownedImage = createImageWithUser(IMAGE_ID, OWNER_USER_ID);
//
//        testImages = Arrays.asList(
//                createImageWithUser(1L, OWNER_USER_ID),
//                createImageWithUser(2L, OWNER_USER_ID),
//                createImageWithUser(3L, OWNER_USER_ID)
//        );
//        testImageIds = testImages.stream()
//                .map(Image::getId)
//                .toList();
//    }
//
//    private void setupValidImageMocks() {
//        // existsById 모킹
//        testImageIds.forEach(id ->
//                when(imageRepository.existsById(id)).thenReturn(true)
//        );
//
//        // findById 모킹
//        testImages.forEach(image ->
//                when(imageRepository.findById(image.getId())).thenReturn(image)
//        );
//    }
//
//    private void setupUnauthorizedImageMocks() {
//        Image unauthorizedImage = createImageWithUser(3L, OTHER_USER_ID);
//        testImages = Arrays.asList(
//                createImageWithUser(1L, OWNER_USER_ID),
//                createImageWithUser(2L, OWNER_USER_ID),
//                unauthorizedImage
//        );
//
//        // existsById 모킹
//        testImageIds.forEach(id ->
//                when(imageRepository.existsById(id)).thenReturn(true)
//        );
//
//        // findById 모킹
//        testImages.forEach(image ->
//                when(imageRepository.findById(image.getId())).thenReturn(image)
//        );
//    }
//
//
//    // 권한 검증 삭제 테스트
//    @Test
//    @DisplayName("TC-004: 권한 검증 정상 삭제 성공")
//    void tc004_deleteById_WithValidUser_Success() {
//        // Given
//        when(imageRepository.findById(IMAGE_ID)).thenReturn(ownedImage);
//        doNothing().when(imageRepository).deleteById(IMAGE_ID);
//
//        // When
//        boolean result = deleteImageCommandService.deleteById(IMAGE_ID, OWNER_USER_ID);
//
//        // Then
//        assertThat(result).isTrue();
//        verify(imageRepository, times(1)).findById(IMAGE_ID);
//        verify(imageRepository, times(1)).deleteById(IMAGE_ID);
//    }
//
//
//
//    @Test
//    @DisplayName("TC-007: 권한 없음 - UnauthorizedException 발생")
//    void tc007_deleteById_Unauthorized_ThrowsException() {
//        // Given
//        when(imageRepository.findById(IMAGE_ID)).thenReturn(ownedImage);
//
//        // When & Then
//        assertThatThrownBy(() -> deleteImageCommandService.deleteById(IMAGE_ID, OTHER_USER_ID))
//                .isInstanceOf(UnauthorizedException.class)
//                .hasMessageContaining("이미지 삭제 권한이 없습니다");
//
//        verify(imageRepository, times(1)).findById(IMAGE_ID);
//        verify(imageRepository, never()).deleteById(any());
//    }
//
//    @Test
//    @DisplayName("TC-008: 정상 다중 삭제 성공")
//    void tc008_deleteAllById_ValidIds_Success() {
//        // Given
//        setupValidImageMocks();
//        doNothing().when(imageRepository).deleteAllById(testImageIds);
//
//        // When
//        assertThatCode(() -> deleteImageCommandService.deleteAllById(testImageIds, OWNER_USER_ID))
//                .doesNotThrowAnyException();
//
//        // Then
//        verify(imageRepository, times(3)).existsById(any());
//        verify(imageRepository, times(3)).findById(any());
//        verify(imageRepository, times(1)).deleteAllById(testImageIds);
//    }
//
//
//    @Test
//    @DisplayName("TC-019: 다중 삭제 시 권한 없는 이미지 포함")
//    void tc019_deleteAllById_UnauthorizedImage_ThrowsException() {
//        // Given
//        setupUnauthorizedImageMocks();
//
//        // When & Then
//        assertThatThrownBy(() -> deleteImageCommandService.deleteAllById(testImageIds, OWNER_USER_ID))
//                .isInstanceOf(UnauthorizedException.class)
//                .hasMessageContaining("이미지 삭제 권한이 없습니다");
//
//        verify(imageRepository, times(3)).existsById(any());
//        verify(imageRepository, atLeastOnce()).findById(any());
//        verify(imageRepository, never()).deleteAllById(any());
//    }
//
//
//    @Test
//    @DisplayName("TC-010: 빈 리스트 - InvalidImageException 발생")
//    void tc010_deleteAllById_EmptyList_ThrowsException() {
//        // Given
//        List<Long> emptyList = Collections.emptyList();
//
//        // When & Then
//        assertThatThrownBy(() -> deleteImageCommandService.deleteAllById(emptyList, OWNER_USER_ID))
//                .isInstanceOf(ImageNotFoundException.class)
//                .hasMessageContaining("찾을 수 있는 이미지가 없습니다.");
//
//        verify(imageRepository, never()).existsById(any());
//    }
//
//
//    @Test
//    @DisplayName("TC-011: 일부 존재하지 않는 ID - 존재하는 것만 삭제")
//    void tc011_deleteAllById_PartiallyExisting_DeletesExistingOnly() {
//        // Given
//        when(imageRepository.existsById(testImageIds.get(0))).thenReturn(true);
//        when(imageRepository.existsById(testImageIds.get(1))).thenReturn(true);
//        when(imageRepository.existsById(testImageIds.get(2))).thenReturn(false);
//
//        // 존재하는 ID만으로 구성된 리스트로 모킹
//        List<Long> existingIds = Arrays.asList(1L, 2L);
//        doNothing().when(imageRepository).deleteAllById(existingIds);
//
//        when(imageRepository.findById(testImageIds.get(0))).thenReturn(createImageWithUser(1L, OWNER_USER_ID));
//        when(imageRepository.findById(testImageIds.get(1))).thenReturn(createImageWithUser(2L, OWNER_USER_ID));
//
//        // When
//        assertThatCode(() -> deleteImageCommandService.deleteAllById(testImageIds, OWNER_USER_ID))
//                .doesNotThrowAnyException();
//
//        // Then
//        verify(imageRepository, times(3)).existsById(any());
//        verify(imageRepository, times(2)).findById(any());
//        verify(imageRepository, times(1)).deleteAllById(existingIds);
//    }
//
//
//    // 게시물별 삭제 테스트
//    @Test
//    @DisplayName("TC-012: 게시물별 정상 삭제 성공")
//    void tc012_deleteByPostId_ValidPostId_Success() {
//        // Given
//        doNothing().when(imageRepository).deleteByPostId(POST_ID);
//
//        // When
//        assertThatCode(() -> deleteImageCommandService.deleteByPostId(POST_ID, OWNER_USER_ID))
//                .doesNotThrowAnyException();
//
//        // Then
//        verify(imageRepository, times(1)).deleteByPostId(POST_ID);
//    }
//
//
//    // 권한 검증 게시물 삭제 테스트
//    @Test
//    @DisplayName("TC-014: 권한 검증 게시물 삭제 성공")
//    void tc014_deleteByPostId_WithValidUser_Success() {
//        // Given
//        List<Image> postImages = Arrays.asList(
//                createImageWithUser(1L, OWNER_USER_ID),
//                createImageWithUser(2L, OWNER_USER_ID)
//        );
//
//        when(imageRepository.findImagesAllByPostId(POST_ID)).thenReturn(postImages);
//
//        // findById 모킹 추가 (getImagesAndCheckOwner에서 사용)
//        when(imageRepository.findById(1L)).thenReturn(postImages.get(0));
//        when(imageRepository.findById(2L)).thenReturn(postImages.get(1));
//
//        doNothing().when(imageRepository).deleteByPostId(POST_ID);
//
//        // When
//        assertThatCode(() -> deleteImageCommandService.deleteByPostId(POST_ID, OWNER_USER_ID))
//                .doesNotThrowAnyException();
//
//        // Then
//        verify(imageRepository, times(1)).findImagesAllByPostId(POST_ID);
//        verify(imageRepository, times(2)).findById(any()); // getImagesAndCheckOwner 호출 확인
//        verify(imageRepository, times(1)).deleteByPostId(POST_ID);
//    }
//
//
//
//    @Test
//    @DisplayName("TC-015: 게시물 이미지 삭제 권한 없음 - UnauthorizedException 발생")
//    void tc015_deleteByPostId_Unauthorized_ThrowsException() {
//        // Given
//        List<Image> postImages = Arrays.asList(
//                createImageWithUser(1L, OWNER_USER_ID),
//                createImageWithUser(2L, OWNER_USER_ID)
//        );
//
//        when(imageRepository.findImagesAllByPostId(POST_ID)).thenReturn(postImages);
//
//        // 첫 번째 이미지에서 권한 체크 실패하므로 하나만 모킹
//        when(imageRepository.findById(1L)).thenReturn(postImages.get(0));
//
//        // When & Then
//        assertThatThrownBy(() -> deleteImageCommandService.deleteByPostId(POST_ID, OTHER_USER_ID))
//                .isInstanceOf(UnauthorizedException.class)
//                .hasMessageContaining("이미지 삭제 권한이 없습니다");
//
//        verify(imageRepository, times(1)).findImagesAllByPostId(POST_ID);
//        verify(imageRepository, times(1)).findById(1L); // 첫 번째 이미지만 확인
//        verify(imageRepository, never()).deleteByPostId(any());
//    }
//
//
//    @Test
//    @DisplayName("TC-016: getImagesAndCheckOwner - 이미지 존재하고 권한 있음")
//    void tc016_getImagesAndCheckOwner_ValidOwner_Success() {
//        // Given
//        when(imageRepository.findById(IMAGE_ID)).thenReturn(ownedImage);
//
//        // When & Then
//        assertThatCode(() -> deleteImageCommandService.deleteById(IMAGE_ID, OWNER_USER_ID))
//                .doesNotThrowAnyException();
//
//        verify(imageRepository, times(1)).findById(IMAGE_ID);
//    }
//
//    @Test
//    @DisplayName("TC-017: getImagesAndCheckOwner - 이미지가 존재하지 않음")
//    void tc017_getImagesAndCheckOwner_ImageNotFound_ThrowsException() {
//        // Given
//        when(imageRepository.findById(IMAGE_ID)).thenReturn(null);
//
//        // When & Then
//        assertThatThrownBy(() -> deleteImageCommandService.deleteById(IMAGE_ID, OWNER_USER_ID))
//                .isInstanceOf(ImageNotFoundException.class)
//                .hasMessageContaining("이미지를 찾을 수 없습니다");
//
//        verify(imageRepository, times(1)).findById(IMAGE_ID);
//        verify(imageRepository, never()).deleteById(any());
//    }
//
//
//
//    // 헬퍼 메소드
//    private Image createImageWithUser(Long imageId, Long userId) {
//        return Image.builder()
//                .id(imageId)
//                .postId(POST_ID)
//                .userId(userId)
//                .imageUrl("https://example.com/image" + imageId + ".jpg")
//                .mimeType("image/jpeg")
//                .build();
//    }
//}