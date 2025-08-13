package backend.airo.domain.post.query;

import backend.airo.api.post.dto.PostListRequest;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GetPostListQueryServiceTest {

    private PostRepository postRepository;
    private GetPostListQueryService service;

    @BeforeEach
    void setUp() {
        postRepository = mock(PostRepository.class);
        service = new GetPostListQueryService(postRepository);
    }

    @Test
    @DisplayName("좋아요 순 조회 테스트")
    void testGetPosts_LikeCount() {
        // given
        PostListRequest request = new PostListRequest(0, 10, null, null, "likeCount", PostStatus.PUBLISHED);
        Page<Post> expectedPage = new PageImpl<>(
                Collections.emptyList(),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "likeCount")),
                0
        );
        when(postRepository.findAllOrderByLikeCountDesc(any())).thenReturn(expectedPage);

        // when
        Page<Post> result = service.handle(request);

        // then
        assertEquals(expectedPage, result);
        verify(postRepository, times(1)).findAllOrderByLikeCountDesc(any());
        verify(postRepository, never()).findAllOrderByViewCountDesc(any());
        verify(postRepository, never()).findByStatus(any(), any());
    }

    @Test
    @DisplayName("조회수 순 조회 테스트")
    void testGetPosts_ViewCount() {
        // given
        PostListRequest request = new PostListRequest(1, 5, null, null, "viewCount", PostStatus.PUBLISHED);
        Page<Post> expectedPage = new PageImpl<>(
                Collections.emptyList(),
                PageRequest.of(1, 5, Sort.by(Sort.Direction.DESC, "viewCount")),
                0
        );
        when(postRepository.findAllOrderByViewCountDesc(any())).thenReturn(expectedPage);

        // when
        Page<Post> result = service.handle(request);

        // then
        assertEquals(expectedPage, result);
        verify(postRepository, times(1)).findAllOrderByViewCountDesc(any());
        verify(postRepository, never()).findAllOrderByLikeCountDesc(any());
        verify(postRepository, never()).findByStatus(any(), any());
    }

    @Test
    @DisplayName("기본 정렬(발행일) 조회 테스트")
    void testGetPosts_DefaultSort_withPublishedAt() {
        // given - sortBy가 "publishedAt"이거나 그 외의 값인 경우 기본 정렬 처리
        PostListRequest request = new PostListRequest(2, 20, null, null, "publishedAt", PostStatus.PUBLISHED);
        Page<Post> expectedPage = new PageImpl<>(
                Collections.emptyList(),
                PageRequest.of(2, 20, Sort.by(Sort.Direction.DESC, "publishedAt")),
                0
        );
        when(postRepository.findByStatus(any(), any())).thenReturn(expectedPage);

        // when
        Page<Post> result = service.handle(request);

        // then
        assertEquals(expectedPage, result);
        verify(postRepository, times(1)).findByStatus(eq(PostStatus.PUBLISHED), any());
        verify(postRepository, never()).findAllOrderByLikeCountDesc(any());
        verify(postRepository, never()).findAllOrderByViewCountDesc(any());
    }

    @Test
    @DisplayName("null sortBy 기본값 적용 테스트")
    void testGetPosts_NullSortBy() {
        // given - PostListRequest에서 null sortBy를 기본값으로 "publishedAt"으로 변환하므로 기본 정렬 적용됨
        PostListRequest request = new PostListRequest(0, 15, null, null, null, null);
        Page<Post> expectedPage = new PageImpl<>(
                Collections.emptyList(),
                PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "publishedAt")),
                0
        );
        when(postRepository.findByStatus(any(), any())).thenReturn(expectedPage);

        // when
        Page<Post> result = service.handle(request);

        // then
        assertEquals(expectedPage, result);
        verify(postRepository, times(1)).findByStatus(eq(PostStatus.PUBLISHED), any());
    }
}