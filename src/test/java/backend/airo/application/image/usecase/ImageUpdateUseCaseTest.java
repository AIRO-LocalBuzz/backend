//package backend.airo.application.image.usecase;
//
//import backend.airo.domain.image.command.DeleteImageCommandService;
//import backend.airo.domain.image.exception.ImageException;
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
//import java.util.stream.Collectors;
//import java.util.stream.LongStream;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("ImageDeleteUseCase 테스트")
//class ImageDeleteUseCaseTest {
//
//    @Mock
//    private DeleteImageCommandService deleteImageCommandService;
//
//    @InjectMocks
//    private ImageDeleteUseCase imageDeleteUseCase;
//
//    private static final Long VALID_USER_ID = 100L;
//    private static final Long INVALID_USER_ID = 999L;
//    private static final Long VALID_IMAGE_ID = 1L;
//    private static final Long VALID_POST_ID = 10L;
//
//
//    // 단일 이미지 삭제 테스트
//    @Test
//    @DisplayName("TC-001: 단일 이미지 삭제 성공")
//    void tc001_deleteImageWithAuth_ValidInput_Success() {
//        // When
//        imageDeleteUseCase.deleteImageWithAuth(VALID_IMAGE_ID, VALID_USER_ID);
//
//        // Then
//        verify(deleteImageCommandService, times(1)).deleteById(VALID_IMAGE_ID, VALID_USER_ID);
//    }
//
//    @Test
//    @DisplayName("TC-004: 단일 이미지 삭제 실패 - 권한 없음")
//    void tc004_deleteImageWithAuth_UnauthorizedUser_ThrowsException() {
//        // Given
//        doThrow(new UnauthorizedException("이미지 삭제"))
//                .when(deleteImageCommandService).deleteById(VALID_IMAGE_ID, INVALID_USER_ID);
//
//        // When & Then
//        assertThatThrownBy(() -> imageDeleteUseCase.deleteImageWithAuth(VALID_IMAGE_ID, INVALID_USER_ID))
//                .isInstanceOf(UnauthorizedException.class);
//
//        verify(deleteImageCommandService, times(1)).deleteById(VALID_IMAGE_ID, INVALID_USER_ID);
//    }
//
//    // 다중 이미지 삭제 테스트
//    @Test
//    @DisplayName("TC-002: 다중 이미지 삭제 성공")
//    void tc002_deleteMultipleImages_ValidInput_Success() {
//        // Given
//        List<Long> imageIds = Arrays.asList(1L, 2L, 3L);
//
//        // When
//        imageDeleteUseCase.deleteMultipleImages(imageIds, VALID_USER_ID);
//
//        // Then
//        verify(deleteImageCommandService, times(1)).deleteAllById(imageIds, VALID_USER_ID);
//    }
//
//
//    @Test
//    @DisplayName("TC-006: 다중 이미지 삭제 - 단일 이미지")
//    void tc006_deleteMultipleImages_SingleImage_Success() {
//        // Given
//        List<Long> singleImageList = List.of(VALID_IMAGE_ID);
//
//        // When
//        imageDeleteUseCase.deleteMultipleImages(singleImageList, VALID_USER_ID);
//
//        // Then
//        verify(deleteImageCommandService, times(1)).deleteAllById(singleImageList, VALID_USER_ID);
//    }
//
//    @Test
//    @DisplayName("TC-008: 대용량 이미지 리스트 삭제")
//    void tc008_deleteMultipleImages_LargeList_Success() {
//        // Given
//        List<Long> largeImageIds = LongStream.rangeClosed(1, 100)
//                .boxed()
//                .collect(Collectors.toList());
//
//        // When
//        imageDeleteUseCase.deleteMultipleImages(largeImageIds, VALID_USER_ID);
//
//        // Then
//        verify(deleteImageCommandService, times(1)).deleteAllById(largeImageIds, VALID_USER_ID);
//        assertThat(largeImageIds).hasSize(100);
//    }
//
//    // 게시물별 이미지 삭제 테스트
//    @Test
//    @DisplayName("TC-003: 게시물별 이미지 삭제 성공")
//    void tc003_deleteImagesByPostWithAuth_ValidInput_Success() {
//        // When
//        imageDeleteUseCase.deleteImagesByPostWithAuth(VALID_POST_ID, VALID_USER_ID);
//
//        // Then
//        verify(deleteImageCommandService, times(1)).deleteByPostId(VALID_POST_ID, VALID_USER_ID);
//    }
//
//    @Test
//    @DisplayName("TC-007: 게시물별 이미지 삭제 실패 - 권한 없음")
//    void tc007_deleteImagesByPostWithAuth_UnauthorizedUser_ThrowsException() {
//        // Given
//        doThrow(new UnauthorizedException("이미지 삭제"))
//                .when(deleteImageCommandService).deleteByPostId(VALID_POST_ID, INVALID_USER_ID);
//
//        // When & Then
//        assertThatThrownBy(() -> imageDeleteUseCase.deleteImagesByPostWithAuth(VALID_POST_ID, INVALID_USER_ID))
//                .isInstanceOf(UnauthorizedException.class);
//
//        verify(deleteImageCommandService, times(1)).deleteByPostId(VALID_POST_ID, INVALID_USER_ID);
//    }
//
//    // 예외 상황 추가 테스트
//    @Test
//    @DisplayName("TC-009: 존재하지 않는 이미지 삭제 시도")
//    void tc009_deleteImageWithAuth_NonExistentImage_ThrowsException() {
//        // Given
//        Long nonExistentImageId = 999L;
//        doThrow(new ImageException(nonExistentImageId))
//                .when(deleteImageCommandService).deleteById(nonExistentImageId, VALID_USER_ID);
//
//        // When & Then
//        assertThatThrownBy(() -> imageDeleteUseCase.deleteImageWithAuth(nonExistentImageId, VALID_USER_ID))
//                .isInstanceOf(ImageException.class);
//
//        verify(deleteImageCommandService, times(1)).deleteById(nonExistentImageId, VALID_USER_ID);
//    }
//
//
//    // 통합 테스트
//    @Test
//    @DisplayName("TC-011: 모든 삭제 방식이 올바른 CommandService 메서드 호출")
//    void tc011_allDeleteMethods_CallCorrectCommandServiceMethods() {
//        // Given
//        Long imageId = 1L;
//        Long postId = 10L;
//        List<Long> imageIds = Arrays.asList(1L, 2L);
//
//        // When
//        imageDeleteUseCase.deleteImageWithAuth(imageId, VALID_USER_ID);
//        imageDeleteUseCase.deleteMultipleImages(imageIds, VALID_USER_ID);
//        imageDeleteUseCase.deleteImagesByPostWithAuth(postId, VALID_USER_ID);
//
//        // Then
//        verify(deleteImageCommandService, times(1)).deleteById(imageId, VALID_USER_ID);
//        verify(deleteImageCommandService, times(1)).deleteAllById(imageIds, VALID_USER_ID);
//        verify(deleteImageCommandService, times(1)).deleteByPostId(postId, VALID_USER_ID);
//
//        // 각 메서드가 정확히 한 번씩만 호출되었는지 확인
//        verifyNoMoreInteractions(deleteImageCommandService);
//    }
//}