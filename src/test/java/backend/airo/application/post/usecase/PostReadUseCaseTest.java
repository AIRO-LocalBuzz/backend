package backend.airo.application.post.usecase;

import backend.airo.api.post.dto.PostDetailResponse;
import backend.airo.api.post.dto.PostListRequest;
import backend.airo.api.post.dto.ThumbnailResponseDto;
import backend.airo.domain.image.Image;
import backend.airo.domain.image.query.GetImageQueryService;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostCategory;
import backend.airo.domain.post.enums.PostEmotionTag;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.query.GetPostListQueryService;
import backend.airo.domain.post.query.GetPostQueryService;
import backend.airo.domain.post.vo.AuthorInfo;
import backend.airo.domain.thumbnail.Thumbnail;
import backend.airo.domain.user.User;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostReadUseCaseTest {

    @Mock private GetPostQueryService getPostQueryService;
    @Mock private GetUserQuery getUserQueryService;
    @Mock private GetImageQueryService getImageQueryService;
    @Mock private GetPostListQueryService getPostListQueryService;

    @InjectMocks private PostUseCase postUseCase;

    private Post mockPost;
    private User mockUser;
    private List<Image> mockImages;
    private Thumbnail mockThumbnail;

    @BeforeEach
    void setUp() {
        mockPost = spy(Post.createForTest(
                1L, 100L, "테스트 게시물", "테스트 내용",
                List.of(PostEmotionTag.EXCITED, PostEmotionTag.FREE), PostCategory.LEISURE, LocalDateTime.now()
        ));
        mockUser = new User(
                100L, "test@example.com", "테스트 사용자", "testuser",
                "010-1234-5678", LocalDate.now().minusYears(20),
                null, null
        );
        mockImages = Arrays.asList(
                new Image(1L, 1L, "image1.jpg", "image/jpeg", 1),
                new Image(2L, 1L, "image2.jpg", "image/jpeg", 2)
        );
        mockThumbnail = Thumbnail.createForTest(1L, "썸네일.jpg");
    }

    // --------------------
    // getPostDetail 메서드 테스트
    // --------------------

    @Test
    @DisplayName("게시물 상세 조회 정상 - 비소유자 요청 시 조회수 증가 및 이미지, 작성자 정보 포함")
    void testGetPostDetail_Success_NonOwner() {
        Long postId = 1L;
        Long requesterId = 200L;
        given(getPostQueryService.handle(postId)).willReturn(mockPost);
        given(getUserQueryService.handle(100L)).willReturn(mockUser);
        given(getImageQueryService.getImagesBelongsPost(postId)).willReturn(mockImages);

        PostDetailResponse response = postUseCase.getPostDetail(postId, requesterId);

        verify(mockPost, times(1)).incrementViewCount();
        assertThat(response).isNotNull();
        assertThat(response.images()).hasSize(2);
        AuthorInfo author = response.author();
        assertThat(author.id()).isEqualTo(100L);
        assertThat(author.nickname()).isEqualTo("테스트 사용자");
    }

    @Test
    @DisplayName("게시물 상세 조회 - 소유자 요청 시 조회수 미증가")
    void testGetPostDetail_NoIncrement_ForOwner() {
        Long postId = 1L;
        Long requesterId = 100L;
        given(getPostQueryService.handle(postId)).willReturn(mockPost);
        given(getUserQueryService.handle(100L)).willReturn(mockUser);
        given(getImageQueryService.getImagesBelongsPost(postId)).willReturn(mockImages);

        PostDetailResponse response = postUseCase.getPostDetail(postId, requesterId);

        verify(mockPost, never()).incrementViewCount();
        assertThat(response).isNotNull();
        AuthorInfo author = response.author();
        assertThat(author.nickname()).isEqualTo("테스트 사용자");
    }

    @Test
    @DisplayName("게시물 상세 조회 - 게스트 요청 시 조회수 증가")
    void testGetPostDetail_Guest_IncrementsView() {
        Long postId = 1L;
        Long requesterId = null;
        given(getPostQueryService.handle(postId)).willReturn(mockPost);
        given(getUserQueryService.handle(100L)).willReturn(mockUser);
        given(getImageQueryService.getImagesBelongsPost(postId)).willReturn(mockImages);

        PostDetailResponse response = postUseCase.getPostDetail(postId, requesterId);

        verify(mockPost, times(1)).incrementViewCount();
        assertThat(response).isNotNull();
        assertThat(response.images()).hasSize(2);
    }

    @Test
    @DisplayName("게시물 상세 조회 - 존재하지 않는 게시물 예외 발생")
    void testGetPostDetail_NotFound() {
        Long postId = 999L;
        Long requesterId = 200L;
        given(getPostQueryService.handle(postId))
                .willThrow(new RuntimeException("게시물을 찾을 수 없습니다"));

        assertThatThrownBy(() -> postUseCase.getPostDetail(postId, requesterId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("게시물을 찾을 수 없습니다");
    }

    // --------------------
    // getRecentPostList (getPostList) 메서드 테스트
    // --------------------

    @Test
    @DisplayName("최근 게시물 목록 조회 - 페이징 처리 및 정렬 확인")
    void testGetRecentPostList_Success() {
        PostListRequest request = new PostListRequest(0, 10, null, null, "publishedAt", PostStatus.PUBLISHED);
        List<Post> posts = Arrays.asList(
                Post.createForTest(1L, 100L, "최신 게시물", "내용1", Collections.emptyList(), PostCategory.LEISURE, LocalDateTime.now()),
                Post.createForTest(2L, 101L, "이전 게시물", "내용2", Collections.emptyList(), PostCategory.LEISURE, LocalDateTime.now().minusMinutes(5))
        );
        Page<Post> expectedPage = new PageImpl<>(posts, PageRequest.of(0, 10), posts.size());
        given(getPostListQueryService.handle(request)).willReturn(expectedPage);

        Page<Post> result = postUseCase.getPostList(request);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        // 정렬: 가장 최근 게시물이 첫번째에 위치
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("최근 게시물 목록 조회 - 빈 결과 반환")
    void testGetRecentPostList_EmptyResult() {
        PostListRequest request = new PostListRequest(0, 10, null, null, "publishedAt", PostStatus.PUBLISHED);
        Page<Post> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        given(getPostListQueryService.handle(request)).willReturn(emptyPage);

        Page<Post> result = postUseCase.getPostList(request);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    // --------------------
    // 유틸리티 메서드 간접 테스트 (isPostOwner, getAuthorInfo)
    // --------------------

    @Test
    @DisplayName("유틸리티 테스트 - isPostOwner 판별")
    void testIsPostOwner() {
        // getPostDetail 내부 호출로 isPostOwner 테스트
        Long postId = 1L;
        Long requesterId = 100L; // 작성자인 경우
        given(getPostQueryService.handle(postId)).willReturn(mockPost);
        given(getUserQueryService.handle(100L)).willReturn(mockUser);
        given(getImageQueryService.getImagesBelongsPost(postId)).willReturn(Collections.emptyList());

        PostDetailResponse response = postUseCase.getPostDetail(postId, requesterId);
        // 작성자 요청 시 조회수 증가는 일어나지 않으므로 이를 통해 소유자 여부 확인
        verify(mockPost, never()).incrementViewCount();
        AuthorInfo author = response.author();
        assertThat(author.id()).isEqualTo(100L);
    }

    @Test
    @DisplayName("유틸리티 테스트 - getAuthorInfo 매핑 확인")
    void testGetAuthorInfoMapping() {
        // getPostDetail를 호출하여 내부 getAuthorInfo 결과 확인
        Long postId = 1L;
        Long requesterId = 200L;
        given(getPostQueryService.handle(postId)).willReturn(mockPost);
        given(getUserQueryService.handle(100L)).willReturn(mockUser);
        given(getImageQueryService.getImagesBelongsPost(postId)).willReturn(Collections.emptyList());

        PostDetailResponse response = postUseCase.getPostDetail(postId, requesterId);
        AuthorInfo author = response.author();
        assertThat(author.id()).isEqualTo(mockUser.getId());
        assertThat(author.nickname()).isEqualTo(mockUser.getName());
        // profileImageUrl 매핑 확인 (null인 경우에도 동일)
        assertThat(author.profileImageUrl()).isEqualTo(mockUser.getProfileImageUrl());
    }

    // 썸네일 조회 테스트 예제
    @Test
    @DisplayName("썸네일 조회 성공")
    void testGetThumbnailById_Success() {
        Long thumbnailId = 1L;
        given(getPostQueryService.handleThumbnail(thumbnailId)).willReturn(mockThumbnail);

        ThumbnailResponseDto dto = postUseCase.getThumbnailById(thumbnailId);

        assertThat(dto).isNotNull();
        assertThat(dto.getMainImageUrl()).isEqualTo("썸네일.jpg");
    }
}