//package backend.airo.application.thumbnail;
//
//import backend.airo.domain.image.repository.ImageRepository;
//import backend.airo.domain.post.Post;
//import backend.airo.domain.post.enums.PostCategory;
//import backend.airo.domain.post.enums.PostEmotionTag;
//import backend.airo.domain.post.event.PostCreatedEvent;
//import backend.airo.domain.thumbnail.LLMProvider;
//import backend.airo.domain.thumbnail.ThumbnailResult;
//import backend.airo.domain.post.vo.Location;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ThumbnailGenerationServiceTest {
//
//    @Mock
//    private LLMProvider llmProvider;
//
//    @Mock
//    private ImageRepository imageRepository;
//
//    private ThumbnailGenerationService thumbnailGenerationService;
//
//    @BeforeEach
//    void setUp() {
//        thumbnailGenerationService = new ThumbnailGenerationService(llmProvider, imageRepository);
//    }
//
//    @Test
//    @DisplayName("게시물 생성 시 썸네일을 자동 생성한다")
//    void shouldGenerateThumbnailWhenPostCreated() {
//        // given
//        Post post = createTestPost();
//        PostCreatedEvent event = new PostCreatedEvent(this, 1L, post);
//        List<String> imageUrls = List.of("image1.jpg", "image2.jpg");
//        ThumbnailResult expectedResult = new ThumbnailResult(
//                "image1.jpg",
//                List.of("태그1", "태그2"),
//                "개선된 제목"
//        );
//
//        when(imageRepository.findImageUrlsByPostId(1L)).thenReturn(imageUrls);
//        when(llmProvider.generateThumbnail(any())).thenReturn(expectedResult);
//
//        // when
//        thumbnailGenerationService.handlePostCreated(event);
//
//        // then
//        verify(imageRepository).findImageUrlsByPostId(1L);
//        verify(llmProvider).generateThumbnail(any());
//    }
//
//    @Test
//    @DisplayName("LLM 호출 실패 시 예외를 처리한다")
//    void shouldHandleExceptionWhenLLMFails() {
//        // given
//        Post post = createTestPost();
//        PostCreatedEvent event = new PostCreatedEvent(this, 1L, post);
//
//        when(imageRepository.findImageUrlsByPostId(1L)).thenReturn(List.of());
//        when(llmProvider.generateThumbnail(any())).thenThrow(new RuntimeException("LLM 오류"));
//
//        // when & then - 예외가 발생해도 메서드가 정상 종료되어야 함
//        thumbnailGenerationService.handlePostCreated(event);
//
//        verify(llmProvider).generateThumbnail(any());
//    }
//
//    private Post createTestPost() {
//        return Post.createForTest(
//                1L,
//                1L,
//                "테스트 제목",
//                "테스트 내용",
//                List.of(PostEmotionTag.HAPPY),
//                PostCategory.CAFE,
//                new Location(37.5665, 126.9780)
//        );
//    }
//}