package backend.airo.domain.post.command;

import backend.airo.api.image.dto.ImageCreateRequest;
import backend.airo.api.post.dto.PostCreateRequest;
import backend.airo.application.image.usecase.ImageCreateUseCase;
import backend.airo.application.thumbnail.ThumbnailGenerationService;
import backend.airo.domain.image.Image;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.*;
import backend.airo.domain.post.exception.PostValidationException;
import backend.airo.domain.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreatePostCommandService 테스트")
class CreatePostCommandServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ImageCreateUseCase imageCreateUseCase;

    @Mock
    private ThumbnailGenerationService thumbnailGenerationService;

    @InjectMocks
    private CreatePostCommandService createPostCommandService;

    private PostCreateRequest validRequest;
    private Post savedPost;
    private final Long VALID_USER_ID = 1L;

    @BeforeEach
    void setUp() {
        validRequest = createValidPostRequest();
        savedPost = createSavedPost();
    }

    // 정상 케이스
    @Test
    @DisplayName("TC-001: 기본 게시물 생성 성공")
    void tc001_handle_ValidRequest_Success() {
        // Given
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        // When
        Post result = createPostCommandService.handle(validRequest, VALID_USER_ID);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("테스트 제목");
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("TC-002: 이미지 포함 게시물 생성 성공")
    void tc002_handle_WithImages_Success() {
        // Given
        List<ImageCreateRequest> imageRequests = Arrays.asList(
                new ImageCreateRequest("https://example.com/image1.jpg", "image/jpeg"),
                new ImageCreateRequest("https://example.com/image2.jpg", "image/jpeg")
        );
        PostCreateRequest requestWithImages = createRequestWithImages(imageRequests);

        List<Image> savedImages = Arrays.asList(
                createImage(1L, 1),
                createImage(2L, 2)
        );

        when(postRepository.save(any(Post.class))).thenReturn(savedPost);
        when(imageCreateUseCase.uploadMultipleImages(any())).thenReturn(savedImages);

        // When
        Post result = createPostCommandService.handle(requestWithImages, VALID_USER_ID);

        // Then
        assertThat(result).isNotNull();
        verify(postRepository).save(any(Post.class));
        verify(imageCreateUseCase).uploadMultipleImages(any());
    }

    @Test
    @DisplayName("TC-003: 썸네일 포함 게시물 생성 성공")
    void tc003_handleWithThumbnail_ValidRequest_Success() {
        // Given
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);
        doNothing().when(thumbnailGenerationService).generateThumbnailAsync(any(Post.class));

        // When
        Post result = createPostCommandService.handleWithThumbnail(validRequest, VALID_USER_ID);

        // Then
        assertThat(result).isNotNull();
        verify(postRepository).save(any(Post.class));
        verify(thumbnailGenerationService).generateThumbnailAsync(savedPost);
    }

    // 요청 검증 테스트
    @Test
    @DisplayName("TC-004: NULL 요청 - PostValidationException 발생")
    void tc004_handle_NullRequest_ThrowsException() {
        // When & Then
        assertThatThrownBy(() -> createPostCommandService.handle(null, VALID_USER_ID))
                .isInstanceOf(PostValidationException.class)
                .hasMessageContaining("게시물 요청은 필수입니다");

        verify(postRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-005: NULL userId - PostValidationException 발생")
    void tc005_handle_NullUserId_ThrowsException() {
        // When & Then
        assertThatThrownBy(() -> createPostCommandService.handle(validRequest, null))
                .isInstanceOf(PostValidationException.class)
                .hasMessageContaining("사용자 ID는 필수입니다");

        verify(postRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-006: 음수 userId - PostValidationException 발생")
    void tc006_handle_NegativeUserId_ThrowsException() {
        // Given
        Long negativeUserId = -1L;

        // When & Then
        assertThatThrownBy(() -> createPostCommandService.handle(validRequest, negativeUserId))
                .isInstanceOf(PostValidationException.class)
                .hasMessageContaining("사용자 ID는 필수입니다");

        verify(postRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-007: NULL 제목 - PostValidationException 발생")
    void tc007_handle_NullTitle_ThrowsException() {
        // Given
        PostCreateRequest invalidRequest = createRequestWithTitle(null);

        // When & Then
        assertThatThrownBy(() -> createPostCommandService.handle(invalidRequest, VALID_USER_ID))
                .isInstanceOf(PostValidationException.class)
                .hasMessageContaining("게시물 제목은 필수입니다");

        verify(postRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-008: 빈 제목 - PostValidationException 발생")
    void tc008_handle_EmptyTitle_ThrowsException() {
        // Given
        PostCreateRequest invalidRequest = createRequestWithTitle("");

        // When & Then
        assertThatThrownBy(() -> createPostCommandService.handle(invalidRequest, VALID_USER_ID))
                .isInstanceOf(PostValidationException.class)
                .hasMessageContaining("게시물 제목은 필수입니다");

        verify(postRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-009: NULL 내용 - PostValidationException 발생")
    void tc009_handle_NullContent_ThrowsException() {
        // Given
        PostCreateRequest invalidRequest = createRequestWithContent(null);

        // When & Then
        assertThatThrownBy(() -> createPostCommandService.handle(invalidRequest, VALID_USER_ID))
                .isInstanceOf(PostValidationException.class)
                .hasMessageContaining("게시물 내용은 필수입니다");

        verify(postRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-010: 빈 내용 - PostValidationException 발생")
    void tc010_handle_EmptyContent_ThrowsException() {
        // Given
        PostCreateRequest invalidRequest = createRequestWithContent("");

        // When & Then
        assertThatThrownBy(() -> createPostCommandService.handle(invalidRequest, VALID_USER_ID))
                .isInstanceOf(PostValidationException.class)
                .hasMessageContaining("게시물 내용은 필수입니다");

        verify(postRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-011: NULL 상태 - PostValidationException 발생")
    void tc011_handle_NullStatus_ThrowsException() {
        // Given
        PostCreateRequest invalidRequest = createRequestWithStatus(null);

        // When & Then
        assertThatThrownBy(() -> createPostCommandService.handle(invalidRequest, VALID_USER_ID))
                .isInstanceOf(PostValidationException.class)
                .hasMessageContaining("게시물 상태는 필수입니다");

        verify(postRepository, never()).save(any());
    }

    // 이미지 처리 테스트
    @Test
    @DisplayName("TC-012: 빈 이미지 리스트 - 정상 처리")
    void tc012_handle_EmptyImageList_Success() {
        // Given
        PostCreateRequest requestWithEmptyImages = createRequestWithImages(Collections.emptyList());
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        // When
        Post result = createPostCommandService.handle(requestWithEmptyImages, VALID_USER_ID);

        // Then
        assertThat(result).isNotNull();
        verify(postRepository).save(any(Post.class));
        verify(imageCreateUseCase, never()).uploadMultipleImages(any());
    }

    @Test
    @DisplayName("TC-013: NULL 이미지 리스트 - 정상 처리")
    void tc013_handle_NullImageList_Success() {
        // Given
        PostCreateRequest requestWithNullImages = createRequestWithImages(null);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        // When
        Post result = createPostCommandService.handle(requestWithNullImages, VALID_USER_ID);

        // Then
        assertThat(result).isNotNull();
        verify(postRepository).save(any(Post.class));
        verify(imageCreateUseCase, never()).uploadMultipleImages(any());
    }

    @Test
    @DisplayName("TC-014: 다중 이미지 순서 정렬 저장")
    void tc014_handle_MultipleImages_OrderedCorrectly() {
        // Given
        List<ImageCreateRequest> imageRequests = Arrays.asList(
                new ImageCreateRequest("https://example.com/image1.jpg", "image/jpeg"),
                new ImageCreateRequest("https://example.com/image2.jpg", "image/jpeg"),
                new ImageCreateRequest("https://example.com/image3.jpg", "image/jpeg")
        );
        PostCreateRequest requestWithImages = createRequestWithImages(imageRequests);

        when(postRepository.save(any(Post.class))).thenReturn(savedPost);
        when(imageCreateUseCase.uploadMultipleImages(any())).thenReturn(Collections.emptyList());

        // When
        createPostCommandService.handle(requestWithImages, VALID_USER_ID);

        // Then
        verify(imageCreateUseCase).uploadMultipleImages(argThat(images -> {
            List<Image> imageList = (List<Image>) images;
            return imageList.size() == 3 &&
                    imageList.get(0).getSortOrder() == 1 &&
                    imageList.get(1).getSortOrder() == 2 &&
                    imageList.get(2).getSortOrder() == 3;
        }));
    }

    // 저장소 오류 테스트
    @Test
    @DisplayName("TC-015: 저장 실패 - RuntimeException 발생")
    void tc015_handle_SaveFailure_ThrowsException() {
        // Given
        when(postRepository.save(any(Post.class)))
                .thenThrow(new RuntimeException("DB 연결 실패"));

        // When & Then
        assertThatThrownBy(() -> createPostCommandService.handle(validRequest, VALID_USER_ID))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("DB 연결 실패");

        verify(postRepository).save(any(Post.class));
        verify(imageCreateUseCase, never()).uploadMultipleImages(any());
    }

    // 헬퍼 메소드
    private PostCreateRequest createValidPostRequest() {
        return new PostCreateRequest(
                "테스트 제목",
                "테스트 내용",
                PostStatus.DRAFT,
                PostWithWhoTag.FRIEND,
                PostForWhatTag.CULINARY,
                List.of(PostEmotionTag.FREE, PostEmotionTag.EXCITED),
                PostCategory.LEISURE,
                LocalDate.now(),
                "서울시 강남구",
                Collections.emptyList(),
                false

        );
    }


    private PostCreateRequest createRequestWithTitle(String title) {
        return new PostCreateRequest(
                title,
                "테스트 내용",
                PostStatus.DRAFT,
                PostWithWhoTag.FRIEND,
                PostForWhatTag.CULINARY,
                List.of(PostEmotionTag.FREE, PostEmotionTag.EXCITED),
                PostCategory.LEISURE,
                LocalDate.now(),
                "서울시 강남구",
                Collections.emptyList(),
                false
        );
    }

    private PostCreateRequest createRequestWithContent(String content) {
        return new PostCreateRequest(
                "테스트 제목",
                content,
                PostStatus.DRAFT,
                PostWithWhoTag.FRIEND,
                PostForWhatTag.CULINARY,
                List.of(PostEmotionTag.FREE, PostEmotionTag.EXCITED),
                PostCategory.LEISURE,
                LocalDate.now(),
                "서울시 강남구",
                Collections.emptyList(),
                false
        );
    }

    private PostCreateRequest createRequestWithStatus(PostStatus status) {
        return new PostCreateRequest(
                "테스트 제목",
                "테스트 내용",
                status,
                PostWithWhoTag.FRIEND,
                PostForWhatTag.CULINARY,
                List.of(PostEmotionTag.FREE, PostEmotionTag.EXCITED),
                PostCategory.LEISURE,
                LocalDate.now(),
                "서울시 강남구",
                Collections.emptyList(),
                false
        );
    }

    private PostCreateRequest createRequestWithImages(List<ImageCreateRequest> images) {
        return new PostCreateRequest(
                "테스트 제목",
                "테스트 내용",
                PostStatus.DRAFT,
                PostWithWhoTag.FRIEND,
                PostForWhatTag.CULINARY,
                List.of(PostEmotionTag.FREE, PostEmotionTag.EXCITED),
                PostCategory.LEISURE,
                LocalDate.now(),
                "서울시 강남구",
                images,
                false
        );
    }

    private Post createSavedPost() {
        return Post.builder()
                .id(1L)
                .userId(VALID_USER_ID)
                .title("테스트 제목")
                .content("테스트 내용")
                .build();
    }

    private Image createImage(Long id, int sortOrder) {
        return Image.builder()
                .id(id)
                .postId(1L)
                .sortOrder(sortOrder)
                .build();
    }
}