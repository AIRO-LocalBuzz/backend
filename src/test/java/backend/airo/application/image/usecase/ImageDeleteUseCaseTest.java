//package backend.airo.application.image.usecase;
//
//import backend.airo.common.exception.NullCheckException;
//import backend.airo.domain.image.Image;
//import backend.airo.domain.image.command.DeleteImageCommandService;
//import backend.airo.domain.image.exception.InvalidImageException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("ImageDeleteUseCaseTest 테스트")
//class ImageDeleteUseCaseTest {
//
////    @Mock
//    private final Class<?> sourceClass = ImageDeleteUseCase.class;
//
//    @Mock
//    private DeleteImageCommandService deleteImageCommandService;
//
//    @InjectMocks
//    private ImageDeleteUseCase imageDeleteUseCase;
//
//    private Image ownedImage;
//    private Image otherUserImage;
//    private final Long OWNER_USER_ID = 1L;
//    private final Long OTHER_USER_ID = 2L;
//    private final Long IMAGE_ID = 100L;
//    private final Long POST_ID = 200L;
//
//    @BeforeEach
//    void setUp() {
//        ownedImage = createImageWithUser(IMAGE_ID, OWNER_USER_ID);
//        otherUserImage = createImageWithUser(IMAGE_ID, OTHER_USER_ID);
//    }
//
//
//    @Test
//    @DisplayName("TC-001: 정상 삭제 성공")
//    void tc001_deleteById_ExistingId_Success() {
//        // Given
//        when(deleteImageCommandService.deleteById(IMAGE_ID, OWNER_USER_ID)).thenReturn(true);
//
//        // When
//        boolean result = imageDeleteUseCase.deleteImageWithAuth(IMAGE_ID, OWNER_USER_ID);
//
//        // Then
//        assertThat(result).isTrue();
//        verify(deleteImageCommandService, times(1)).deleteById(IMAGE_ID, OWNER_USER_ID);
//    }
//
//
//    @Test
//    @DisplayName("TC-002: NULL ID 처리 - InvalidImageException 발생")
//    void tc002_deleteById_NullId_ThrowsException() {
//        // Given
//        Long nullId = null;
//
//        // When & Then
//        assertThatThrownBy(() -> imageDeleteUseCase.deleteImageWithAuth(nullId, OWNER_USER_ID))
//                .isInstanceOf(NullCheckException.class)
//                .hasMessageContaining("이미지 ID는 필수입니다");
//
//        verify(deleteImageCommandService, never()).deleteById(nullId, OWNER_USER_ID);
//    }
//
//
//
//    @Test
//    @DisplayName("TC-006: NULL 사용자 ID - InvalidImageException 발생")
//    void tc006_deleteById_NullUserId_ThrowsException() {
//        // Given
//        Long nullUserId = null;
//
//        // When & Then
//        assertThatThrownBy(() -> imageDeleteUseCase.deleteImageWithAuth(IMAGE_ID, nullUserId))
//                .isInstanceOf(NullCheckException.class)
//                .hasMessageContaining("사용자 ID는 필수입니다");
//
//        verify(deleteImageCommandService, never()).deleteById(IMAGE_ID, nullUserId);
//    }
//
//    @Test
//    @DisplayName("TC-009: NULL 리스트 - InvalidImageException 발생")
//    void tc009_deleteAllById_NullList_ThrowsException() {
//        // Given
//        List<Long> nullList = null;
//
//        // When & Then
//        assertThatThrownBy(() -> imageDeleteUseCase.deleteMultipleImages(nullList, OWNER_USER_ID))
//                .isInstanceOf(NullCheckException.class)
//                .hasMessageContaining("이미지 ID 목록은 필수입니다");
//
//        verify(deleteImageCommandService, never()).deleteAllById(nullList, OWNER_USER_ID);
//    }
//
//    @Test
//    @DisplayName("TC-013: NULL 게시물 ID - InvalidImageException 발생")
//    void tc013_deleteByPostId_NullPostId_ThrowsException() {
//        // Given
//        Long nullPostId = null;
//
//        // When & Then
//        assertThatThrownBy(() -> imageDeleteUseCase.deleteImagesByPostWithAuth(nullPostId, OWNER_USER_ID))
//                .isInstanceOf(NullCheckException.class)
//                .hasMessageContaining("게시물 ID는 필수입니다");
//
//        verify(deleteImageCommandService, never()).deleteByPostId(nullPostId, OWNER_USER_ID);
//    }
//
//
//
//
//    private Image createImageWithUser(Long imageId, Long userId) {
//        return Image.builder()
//                .id(imageId)
//                .userId(userId)
//                .imageUrl("https://example.com/image" + imageId + ".jpg")
//                .mimeType("image/jpeg")
//                .isCover(false)
//                .build();
//
//    }
//}