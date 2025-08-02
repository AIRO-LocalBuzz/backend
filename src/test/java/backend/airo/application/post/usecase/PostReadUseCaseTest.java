package backend.airo.application.post.usecase;

import backend.airo.api.post.dto.PostDetailResponse;
import backend.airo.api.post.dto.PostListRequest;
import backend.airo.domain.image.Image;
import backend.airo.domain.image.query.GetImageQueryService;
import backend.airo.domain.location.Location;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostCategory;
import backend.airo.domain.post.enums.PostEmotionTag;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.query.GetPostListQueryService;
import backend.airo.domain.post.query.GetPostQueryService;
import backend.airo.domain.post.vo.AuthorInfo;
import backend.airo.domain.user.User;
import backend.airo.domain.user.enums.ProviderType;
import backend.airo.domain.user.query.GetUserQuery;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PostReadUseCaseTest {

    @Mock private GetPostQueryService getPostQueryService;
    @Mock private GetUserQuery getUserQueryService;
    @Mock private GetImageQueryService getImageQueryService;
    @Mock private GetPostListQueryService getPostListQueryService;

    @InjectMocks private PostReadUseCase postReadUseCase;

    private Post mockPost;
    private User mockUser;
    private List<Image> mockImages;

    @BeforeEach
    void setUp() {
        Post realPost = Post.createForTest(
                1L,
                100L,
                "테스트 게시물",
                "테스트 내용",
                List.of(PostEmotionTag.HAPPY, PostEmotionTag.EXCITED),
                PostCategory.CAFE,
                LocalDateTime.now()
        );

        mockPost = spy(realPost);

        mockUser = new User(
                100L,
                "test@example.com",
                "테스트 사용자",
                "testuser",
                "010-1234-5678",
                LocalDate.now().minusYears(20),
                ProviderType.KAKAO,
                "kakao_provider_id"
        );


        mockImages = Arrays.asList(
                new Image(1L, 1L, "image1.jpg", "jpeg/image", 1),
                new Image(1L, 1L, "image1.jpg", "jpeg/image", 1)
        );
    }

    @Test
    @DisplayName("게시물 조회 성공")
    void getPostById_Success() {
        // given
        Long postId = 1L;
        Long requesterId = 200L; // 다른 사용자

        given(getPostQueryService.handle(postId)).willReturn(mockPost);
        given(getUserQueryService.handle(100L)).willReturn(mockUser);
        given(getImageQueryService.getImagesBelongsPost(postId)).willReturn(mockImages);

        // when
        PostDetailResponse response = postReadUseCase.getPostById(postId, requesterId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("테스트 게시물");
        assertThat(response.author().id()).isEqualTo(100L);
        assertThat(response.author().nickname()).isEqualTo("테스트 사용자");
        assertThat(response.images()).hasSize(2);
    }

    @Test
    @DisplayName("작성자가 아닌 사용자 조회 시 조회수 증가")
    void getPostById_IncrementViewCount_WhenNotOwner() {
        // given
        Long postId = 1L;
        Long requesterId = 200L; // 다른 사용자

        given(mockPost.getUserId()).willReturn(100L);
        given(getPostQueryService.handle(postId)).willReturn(mockPost);
        given(getUserQueryService.handle(100L)).willReturn(mockUser);
        given(getImageQueryService.getImagesBelongsPost(postId)).willReturn(mockImages);

        // when
        postReadUseCase.getPostById(postId, requesterId);

        // then
        verify(mockPost, times(1)).incrementViewCount();
    }

    @Test
    @DisplayName("작성자 본인 조회 시 조회수 미증가")
    void getPostById_NoIncrementViewCount_WhenOwner() {
        // given
        Long postId = 1L;
        Long requesterId = 100L; // 작성자 본인

        given(mockPost.getUserId()).willReturn(100L);
        given(getPostQueryService.handle(postId)).willReturn(mockPost);
        given(getUserQueryService.handle(100L)).willReturn(mockUser);
        given(getImageQueryService.getImagesBelongsPost(postId)).willReturn(mockImages);

        // when
        postReadUseCase.getPostById(postId, requesterId);

        // then
        verify(mockPost, never()).incrementViewCount();
    }


    @Test
    @DisplayName("게스트 사용자 조회 시 조회수 증가")
    void getPostById_IncrementViewCount_WhenGuest() {
        // given
        Long postId = 1L;
        Long requesterId = null; // 게스트

        given(getPostQueryService.handle(postId)).willReturn(mockPost);
        given(getUserQueryService.handle(100L)).willReturn(mockUser);
        given(getImageQueryService.getImagesBelongsPost(postId)).willReturn(mockImages);

        // when
        postReadUseCase.getPostById(postId, requesterId);

        // then
        verify(mockPost, times(1)).incrementViewCount();
    }

    @Test
    @DisplayName("존재하지 않는 게시물 조회 시 예외 발생")
    void getPostById_ThrowException_WhenPostNotFound() {
        // given
        Long postId = 999L;
        Long requesterId = 200L;

        given(getPostQueryService.handle(postId))
                .willThrow(new RuntimeException("게시물을 찾을 수 없습니다"));

        // when & then
        assertThatThrownBy(() -> postReadUseCase.getPostById(postId, requesterId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("게시물을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("이미지가 없는 게시물 조회")
    void getPostById_WithNoImages() {
        // given
        Long postId = 1L;
        Long requesterId = 200L;

        given(getPostQueryService.handle(postId)).willReturn(mockPost);
        given(getUserQueryService.handle(100L)).willReturn(mockUser);
        given(getImageQueryService.getImagesBelongsPost(postId)).willReturn(Collections.emptyList());

        // when
        PostDetailResponse response = postReadUseCase.getPostById(postId, requesterId);

        // then
        assertThat(response.images()).isEmpty();
    }




    @Test
    @DisplayName("최근 게시물 목록 조회 성공")
    void getRecentPostList_Success() {
        // given
        PostListRequest request = new PostListRequest(0, 10, null, null);
        LocalDateTime baseTime = LocalDateTime.now();

        List<Post> posts = Arrays.asList(
                Post.createForTest(1L, 100L, "첫 번째 게시물", "내용1", List.of(PostEmotionTag.HAPPY), PostCategory.CAFE, baseTime.minusMinutes(1)),
                Post.createForTest(2L, 101L, "두 번째 게시물", "내용2", List.of(PostEmotionTag.EXCITED), PostCategory.RESTAURANT, baseTime.minusMinutes(5)),
                Post.createForTest(3L, 102L, "세 번째 게시물", "내용3", List.of(PostEmotionTag.PEACEFUL), PostCategory.CHALLENGE, baseTime.minusMinutes(10)),
                Post.createForTest(4L, 103L, "네 번째 게시물", "내용4", List.of(PostEmotionTag.HAPPY, PostEmotionTag.EXCITED), PostCategory.LEISURE, baseTime.minusMinutes(15)),
                Post.createForTest(5L, 104L, "다섯 번째 게시물", "내용5", List.of(PostEmotionTag.RELAXED), PostCategory.LEISURE, baseTime.minusMinutes(20)),
                Post.createForTest(6L, 105L, "여섯 번째 게시물", "내용6", List.of(PostEmotionTag.JOYFUL), PostCategory.CAFE, baseTime.minusMinutes(25)),
                Post.createForTest(7L, 106L, "일곱 번째 게시물", "내용7", List.of(PostEmotionTag.CALM), PostCategory.NATURE, baseTime.minusMinutes(30)),
                Post.createForTest(8L, 107L, "여덟 번째 게시물", "내용8", List.of(PostEmotionTag.HAPPY), PostCategory.EVENT, baseTime.minusMinutes(35)),
                Post.createForTest(9L, 108L, "아홉 번째 게시물", "내용9", List.of(PostEmotionTag.RELAXED), PostCategory.ACCOMMODATION, baseTime.minusMinutes(40)),
                Post.createForTest(10L, 109L, "열 번째 게시물", "내용10", List.of(PostEmotionTag.CALM), PostCategory.CAFE, baseTime.minusMinutes(45))
        );


        Page<Post> expectedPage = new PageImpl<>(posts, PageRequest.of(0, 10), 10);

        given(getPostListQueryService.getRecentPosts(request)).willReturn(expectedPage);

        // when
        Page<Post> result = postReadUseCase.getRecentPostList(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(10);
        assertThat(result.getTotalElements()).isEqualTo(10);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
        assertThat(result.getContent().get(0).getPublishedAt()).isAfter(result.getContent().get(9).getPublishedAt());
    }

    @Test
    @DisplayName("게시물이 없을 때 빈 페이지 반환")
    void getRecentPostList_EmptyResult() {
        // given
        PostListRequest request = new PostListRequest(0, 10, null, null);
        Page<Post> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);

        given(getPostListQueryService.getRecentPosts(request)).willReturn(emptyPage);

        // when
        Page<Post> result = postReadUseCase.getRecentPostList(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("페이징 파라미터 정상 전달")
    void getRecentPostList_PagingParameters() {
        // given
        PostListRequest request = new PostListRequest(0, 10, null, null);
        Page<Post> expectedPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(1, 5), 0);

        given(getPostListQueryService.getRecentPosts(request)).willReturn(expectedPage);

        // when
        Page<Post> result = postReadUseCase.getRecentPostList(request);

        // then
        verify(getPostListQueryService).getRecentPosts(request);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(1);
        assertThat(result.getPageable().getPageSize()).isEqualTo(5);
    }
}