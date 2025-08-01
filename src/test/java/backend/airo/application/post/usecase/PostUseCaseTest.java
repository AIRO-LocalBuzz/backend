//package backend.airo.application.post.usecase;
//
//import backend.airo.api.post.dto.PostCreateRequest;
//import backend.airo.domain.post.command.CreatePostCommandService;
//import backend.airo.domain.post.command.UpdatePostCommand;
//import backend.airo.domain.post.command.DeletePostCommand;
//import backend.airo.domain.post.query.GetPostQueryService;
//import backend.airo.domain.post.query.GetPostListQuery;
//import backend.airo.domain.post.Post;
//import backend.airo.domain.post.repository.PostRepository;
//import backend.airo.domain.post.enums.PostStatus;
//import backend.airo.domain.post.events.PostStatusChangedEvent;
//import backend.airo.domain.post.exception.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.BDDMockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("PostUseCase 테스트")
//class PostUseCaseTest {
//
//    @Mock
//    private PostRepository postRepository;
//
//    @Mock
//    private ApplicationEventPublisher eventPublisher;
//
//    @Mock
//    private CreatePostCommandService createPostCommandService;
//
//    @InjectMocks
//    private PostCreateUseCase postUseCase;
//
//    private Post testPost;
//    private PostCreateRequest postCreateRequest;
//    private UpdatePostCommand updateCommand;
//    private DeletePostCommand deleteCommand;
//    private GetPostQueryService getPostQueryService;
//    private GetPostListQuery getPostListQuery;
//
//
//    @BeforeEach
//    void setup() {
//        testPost = createTestPost();
//        postCreateRequest = PostCreateRequest.forDraft("테스트 제목", "테스트 내용", 1L);
////        createPublishCommand = CreatePostCommand.forPublish("테스트 제목", "테스트 내용", 1L, 1L, 1L);
//        updateCommand = UpdatePostCommand.updateTitle(1L, 1L, "수정된 제목");
//        deleteCommand = DeletePostCommand.of(1L, 1L);
//        getPostQueryService = GetPostQueryService.of(1L);
//        getPostListQuery = GetPostListQuery.defaultQuery();
//    }
//
//    @Nested
//    @DisplayName("게시물 생성 테스트")
//    class CreatePostTest {
//
//        @Test
//        @DisplayName("TC-001: 정상적인 임시저장 게시물 생성")
//        void createDraftPost() {
//            // given
//            given(createPostCommandService.handle(any(PostCreateRequest.class))).willReturn(testPost);
//
//            // when
//            Post result = postUseCase.createPost(postCreateRequest);
//
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result.getStatus()).isEqualTo(PostStatus.DRAFT);
//
//            verify(createPostCommandService).handle(postCreateRequest);
//        }
//
//        @Test
//        @DisplayName("TC-002: 정상적인 발행 게시물 생성")
//        void createPublishedPost() {
//            // given
//            Post publishedPost = createTestPost(PostStatus.PUBLISHED);
//            given(createPostCommandService.handle(any(PostCreateRequest.class))).willReturn(publishedPost);
//
//            // when
//            Post result = postUseCase.createPost(postCreateRequest);
//
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result.getStatus()).isEqualTo(PostStatus.PUBLISHED);
//            assertThat(result.getPublishedAt()).isNotNull();
//
//            verify(createPostCommandService).handle(any(PostCreateRequest.class));
////            verify(eventPublisher).publishEvent(any(PostPublishedEvent.class));
//        }
//
//        @Test
//        @DisplayName("TC-003: 발행 조건 미충족 시 예외 발생")
//        void createPostWithInvalidPublishCondition() {
//            // given
//            PostCreateRequest invalidCommand = new PostCreateRequest(
//                    "제목", "내용", 1L, PostStatus.PUBLISHED,
//                    null, null, null, List.of(), List.of(), false // categoryId, locationId 누락
//            );
//
//            // when & then
//
//            assertThatThrownBy(() -> postUseCase.createPost(invalidCommand))
//                    .isInstanceOf(PostPublishException.class)
//                    .hasMessageContaining("발행에 필요한 필수 정보가 누락되었습니다");
//        }
//
//        @Test
//        @DisplayName("TC-005: 이미지 연결과 함께 게시물 생성")
//        void createPostWithImages() {
//            // given
//            List<Long> imageIds = List.of(1L, 2L, 3L);
//            PostCreateRequest commandWithImages = new PostCreateRequest(
//                    "제목", "내용", 1L, PostStatus.DRAFT,
//                    null, null, null, imageIds, List.of(), false
//            );
//            given(createPostCommandService.handle(any(PostCreateRequest.class))).willReturn(testPost);
//
//            // when
//            Post result = postUseCase.createPost(postCreateRequest);
//
//            // then
//            assertThat(result).isNotNull();
//            verify(createPostCommandService).handle(any(PostCreateRequest.class));
//            // 이미지 연결 로직이 실행되었는지 검증 (실제 구현에 따라 조정)
//        }
//    }
//
//    @Nested
//    @DisplayName("게시물 조회 테스트")
//    class GetPostTest {
//
//        @Test
//        @DisplayName("TC-006: ID로 게시물 정상 조회")
//        void getPostById() {
//            // given
//            // testPost를 PUBLISHED 상태로 설정하여 접근 권한 검증 통과
//            Post publishedPost = createTestPost(PostStatus.PUBLISHED);
//            given(createPostCommandService.handle(any(PostCreateRequest.class))).willReturn(publishedPost);
//
//            // when
//            Post result = postUseCase.getPostById(getPostQueryService);
//
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result.getId()).isEqualTo(1L);
//            verify(postRepository).findById(1L);
//        }
//
//        @Test
//        @DisplayName("TC-007: 존재하지 않는 게시물 조회")
//        void getPostByIdNotFound() {
//            // given
//            given(postRepository.findById(1L)).willThrow(new PostNotFoundException(1L));
//
//            // when & then
//            assertThatThrownBy(() -> postUseCase.getPostById(getPostQueryService))
//                    .isInstanceOf(PostNotFoundException.class);
//
//            // 불필요한 verify 제거 또는 lenient 사용
//        }
//
//        @Test
//        @DisplayName("TC-008: 조회수 증가와 함께 게시물 조회")
//        void getPostByIdWithViewCountIncrement() {
//            // given
//            GetPostQueryService queryWithViewIncrement = GetPostQueryService.byUser(1L, 2L);
//            Post publishedPost = createTestPost(PostStatus.PUBLISHED);
//            given(postRepository.findById(1L)).willReturn(publishedPost);
//            given(postRepository.incrementViewCount(1L, 1)).willReturn(true);
//
//            // when
//            Post result = postUseCase.getPostById(queryWithViewIncrement);
//
//            // then
//            assertThat(result).isNotNull();
//            verify(postRepository).findById(1L);
//            verify(postRepository).incrementViewCount(1L, 1);
//        }
//
//        @Test
//        @DisplayName("TC-009: 연관 데이터 포함 게시물 조회")
//        void getPostWithDetails() {
//            // given
//            GetPostQueryService detailQuery = GetPostQueryService.withDetails(1L);
//            Post publishedPost = createTestPost(PostStatus.PUBLISHED);
//            given(postRepository.findByIdWithAssociations(1L, true, true, true))
//                    .willReturn(Optional.of(publishedPost));
//
//            // when
//            Post result = postUseCase.getPostById(detailQuery);
//
//            // then
//            assertThat(result).isNotNull();
//            verify(postRepository).findByIdWithAssociations(1L, true, true, true);
//        }
//
//        @Test
//        @DisplayName("TC-010: 권한 없는 사용자의 비공개 게시물 조회")
//        void getPrivatePostByUnauthorizedUser() {
//            // given
//            Post draftPost = new Post(1L, 1L, 1L, 1L, "제목", "내용", "요약", PostStatus.DRAFT,
//                    LocalDateTime.now(), 0, 0, 0, false, null);
//            GetPostQueryService unauthorizedQuery = GetPostQueryService.byUser(1L, 2L); // 다른 사용자
//            given(postRepository.findById(1L)).willReturn(draftPost);
//
//            // when & then
//            assertThatThrownBy(() -> postUseCase.getPostById(unauthorizedQuery))
//                    .isInstanceOf(PostAccessDeniedException.class);
//        }
//    }
//
//    @Nested
//    @DisplayName("게시물 목록 조회 테스트")
//    class GetPostListTest {
//
//        @Test
//        @DisplayName("TC-011: 기본 게시물 목록 조회")
//        void getPostList() {
//            // given
//            List<Post> posts = List.of(testPost);
//            Page<Post> postPage = new PageImpl<>(posts, PageRequest.of(0, 20), 1);
//            given(postRepository.findPublishedPosts(any(Pageable.class))).willReturn(postPage);
//
//            // when
//            Page<Post> result = postUseCase.getPostList(getPostListQuery);
//
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result.getContent()).hasSize(1);
//            verify(postRepository).findPublishedPosts(any(Pageable.class));
//        }
//
//        @Test
//        @DisplayName("TC-012: 사용자별 게시물 목록 조회")
//        void getPostListByUser() {
//            // given
//            GetPostListQuery userQuery = GetPostListQuery.byUser(1L);
//            List<Post> posts = List.of(testPost);
//            Page<Post> postPage = new PageImpl<>(posts, PageRequest.of(0, 20), 1);
//
//            given(postRepository.findByUserId(eq(1L), any(Pageable.class))).willReturn(postPage);
//            // 불필요한 Mock 제거
//
//            // when
//            Page<Post> result = postUseCase.getPostList(userQuery);
//
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result.getContent()).hasSize(1);
//            verify(postRepository).findByUserId(eq(1L), any(Pageable.class));
//        }
//
//        @Test
//        @DisplayName("TC-014: 키워드 검색 게시물 목록 조회")
//        void getPostListByKeyword() {
//            // given
//            GetPostListQuery searchQuery = GetPostListQuery.search("여행");
//            List<Post> posts = List.of(testPost);
//            Page<Post> postPage = new PageImpl<>(posts, PageRequest.of(0, 20), 1);
//            given(postRepository.searchFullText(eq("여행"), any(Pageable.class))).willReturn(postPage);
//
//            // when
//            Page<Post> result = postUseCase.getPostList(searchQuery);
//
//            // then
//            assertThat(result).isNotNull();
//            verify(postRepository).searchFullText(eq("여행"), any(Pageable.class));
//        }
//    }
//
//    @Nested
//    @DisplayName("게시물 수정 테스트")
//    class UpdatePostTest {
//
//        @Test
//        @DisplayName("TC-018: 정상적인 게시물 제목/내용 수정")
//        void updatePost() {
//            // given
//            given(postRepository.findById(1L)).willReturn(testPost);
//            given(postRepository.save(any(Post.class))).willReturn(testPost);
//
//            // when
//            Post result = postUseCase.updatePost(updateCommand);
//
//            // then
//            assertThat(result).isNotNull();
//            verify(postRepository).findById(1L);
//            verify(postRepository).save(any(Post.class));
//        }
//
//        @Test
//        @DisplayName("TC-019: 권한 없는 사용자의 게시물 수정 시도")
//        void updatePostByUnauthorizedUser() {
//            // given
//            UpdatePostCommand unauthorizedCommand = UpdatePostCommand.updateTitle(1L, 2L, "수정 제목");
//            given(postRepository.findById(1L)).willReturn(testPost);
//
//            // when & then
//            assertThatThrownBy(() -> postUseCase.updatePost(unauthorizedCommand))
//                    .isInstanceOf(PostAccessDeniedException.class);
//        }
//
//        @Test
//        @DisplayName("TC-020: 존재하지 않는 게시물 수정 시도")
//        void updateNonExistentPost() {
//            // given
//            given(postRepository.findById(1L)).willThrow(new PostNotFoundException(1L));
//
//            // when & then
//            assertThatThrownBy(() -> postUseCase.updatePost(updateCommand))
//                    .isInstanceOf(PostNotFoundException.class);
//        }
//
//        @Test
//        @DisplayName("TC-021: 게시물 상태 변경 (DRAFT → PUBLISHED)")
//        void updatePostStatusToPublished() {
//            // given
//            // 발행 조건을 만족하는 Post 객체 생성 (제목, 내용이 있는 상태)
//            Post draftPost = new Post(1L, 1L, 1L, 1L, "제목", "내용", "요약", PostStatus.DRAFT,
//                    LocalDateTime.now(), 0, 0, 0, false, null);
//            UpdatePostCommand statusCommand = UpdatePostCommand.publish(1L, 1L, 1L,  1L );
//            given(postRepository.findById(1L)).willReturn(draftPost);
//            given(postRepository.save(any(Post.class))).willReturn(draftPost);
//
//            // when
//            Post result = postUseCase.updatePost(statusCommand);
//
//            // then
//            assertThat(result).isNotNull();
//            verify(eventPublisher).publishEvent(any(PostStatusChangedEvent.class));
//        }
//
//        @Test
//        @DisplayName("TC-023: 발행 조건 미충족 상태로 발행 시도")
//        void updatePostToPublishedWithInvalidCondition() {
//            // given
//            Post invalidPost = new Post(1L, 1L,null, null,"제목", "내용", "요약", PostStatus.DRAFT,
//                    LocalDateTime.now(), 0, 0, 0, false, null);
//            // categoryId, locationId가 null인 상태
//
//            UpdatePostCommand publishCommand = UpdatePostCommand.publish(1L, 1L, null, null);
//            given(postRepository.findById(1L)).willReturn(invalidPost);
//            // save 호출되지 않도록 Mock 설정하지 않음
//
//            // when & then
//            assertThatThrownBy(() -> postUseCase.updatePost(publishCommand))
//                    .isInstanceOf(PostPublishException.class);
//        }
//    }
//
//    @Nested
//    @DisplayName("게시물 삭제 테스트")
//    class DeletePostTest {
//
//        @Test
//        @DisplayName("TC-024: 정상적인 게시물 삭제")
//        void deletePost() {
//            // given
//            given(postRepository.findById(1L)).willReturn(testPost);
//
//            // when
//            postUseCase.deletePost(deleteCommand);
//
//            // then
//            verify(postRepository).findById(1L);
//            verify(postRepository).deleteById(1L);
//        }
//
//        @Test
//        @DisplayName("TC-025: 권한 없는 사용자의 게시물 삭제 시도")
//        void deletePostByUnauthorizedUser() {
//            // given
//            DeletePostCommand unauthorizedCommand = DeletePostCommand.of(1L, 2L);
//            given(postRepository.findById(1L)).willReturn(testPost);
//
//            // when & then
//            assertThatThrownBy(() -> postDeleteUseCase.deletePost(unauthorizedCommand))
//                    .isInstanceOf(PostAccessDeniedException.class);
//        }
//
//        @Test
//        @DisplayName("TC-026: 존재하지 않는 게시물 삭제 시도")
//        void deleteNonExistentPost() {
//            // given
//            given(postRepository.findById(1L)).willThrow(new PostNotFoundException(1L));
//
//            // when & then
//            assertThatThrownBy(() -> postDeleteUseCase.deletePost(deleteCommand))
//                    .isInstanceOf(PostNotFoundException.class);
//        }
//
//        @Test
//        @DisplayName("TC-027: 발행된 게시물 삭제 제한")
//        void deletePublishedPostWithoutForce() {
//            // given
//            Post publishedPost = createTestPost(PostStatus.PUBLISHED);
//            given(postRepository.findById(1L)).willReturn(publishedPost);
//
//            // when & then
//            assertThatThrownBy(() -> postUseCase.deletePost(deleteCommand))
//                    .isInstanceOf(PostDeleteException.class)
//                    .hasMessageContaining("발행된 게시물은 삭제할 수 없습니다");
//        }
//
//        @Test
//        @DisplayName("TC-028: 관리자 권한으로 강제 삭제")
//        void forceDeletePost() {
//            // given
//            Post publishedPost = createTestPost(PostStatus.PUBLISHED);
//            DeletePostCommand forceCommand = DeletePostCommand.forceDelete(1L, 1L, "관리자 삭제");
//            given(postRepository.findById(1L)).willReturn(publishedPost);
//
//            // when
//            postUseCase.deletePost(forceCommand);
//
//            // then
//            verify(postRepository).deleteById(1L);
//        }
//    }
//
//    @Nested
//    @DisplayName("게시물 좋아요 테스트")
//    class LikePostTest {
//
//        @Test
//        @DisplayName("TC-029: 정상적인 좋아요 추가")
//        void likePost() {
//            // given
//            given(postRepository.findById(1L)).willReturn(testPost);
//            given(postRepository.updateLikeCount(1L, 1)).willReturn(true);
//
//            // when
//            postUseCase.likePost(1L, 2L);
//
//            // then
//            verify(postRepository).findById(1L);
//            verify(postRepository).updateLikeCount(1L, 1);
//        }
//
//        @Test
//        @DisplayName("TC-031: 존재하지 않는 게시물에 좋아요 시도")
//        void likeNonExistentPost() {
//            // given
//            given(postRepository.findById(1L)).willThrow(new PostNotFoundException(1L));
//
//            // when & then
//            assertThatThrownBy(() -> postUseCase.likePost(1L, 2L))
//                    .isInstanceOf(PostNotFoundException.class);
//        }
//    }
//
//    @Nested
//    @DisplayName("통계 및 집계 테스트")
//    class StatisticsTest {
//
//        @Test
//        @DisplayName("TC-034: 인기 게시물 목록 조회")
//        void getPopularPosts() {
//            // given
//            List<Post> popularPosts = List.of(testPost);
//            given(postRepository.findPopularPosts(10)).willReturn(popularPosts);
//
//            // when
//            List<Post> result = postUseCase.getPopularPosts(10);
//
//            // then
//            assertThat(result).hasSize(1);
//            verify(postRepository).findPopularPosts(10);
//        }
//
//        @Test
//        @DisplayName("TC-035: 최근 인기 게시물 목록 조회")
//        void getRecentPopularPosts() {
//            // given
//            List<Post> recentPosts = List.of(testPost);
//            given(postRepository.findRecentPopularPosts(7, 10)).willReturn(recentPosts);
//
//            // when
//            List<Post> result = postUseCase.getRecentPopularPosts(7, 10);
//
//            // then
//            assertThat(result).hasSize(1);
//            verify(postRepository).findRecentPopularPosts(7, 10);
//        }
//    }
//
//    @Nested
//    @DisplayName("배치 처리 테스트")
//    class BatchOperationTest {
//
//        @Test
//        @DisplayName("TC-036: 여러 게시물 상태 일괄 변경")
//        void updatePostStatusBatch() {
//            // given
//            List<Long> postIds = List.of(1L, 2L, 3L);
//            given(postRepository.updateStatusBatch(postIds, PostStatus.ARCHIVED)).willReturn(3);
//
//            // when
//            int result = postUseCase.updatePostStatusBatch(postIds, PostStatus.ARCHIVED);
//
//            // then
//            assertThat(result).isEqualTo(3);
//            verify(postRepository).updateStatusBatch(postIds, PostStatus.ARCHIVED);
//        }
//
//        @Test
//        @DisplayName("TC-037: 오래된 임시저장 게시물 정리")
//        void cleanupOldDraftPosts() {
//            // given
//            given(postRepository.deleteOldDraftPosts(30)).willReturn(5);
//
//            // when
//            int result = postUseCase.cleanupOldDraftPosts(30);
//
//            // then
//            assertThat(result).isEqualTo(5);
//            verify(postRepository).deleteOldDraftPosts(30);
//        }
//    }
//
//    @Nested
//    @DisplayName("이벤트 발행 테스트")
//    class EventTest {
////
////        @Test
////        @DisplayName("TC-042: 게시물 발행 시 이벤트 발행")
////        void publishEventOnPostCreation() {
////            // given
////            Post publishedPost = createTestPost(PostStatus.PUBLISHED);
////            given(postRepository.save(any(Post.class))).willReturn(publishedPost);
////
////            // when
////            postUseCase.createPost(createPublishCommand);
////
////            // then
////            verify(eventPublisher).publishEvent(any(PostPublishedEvent.class));
////        }
//
//        @Test
//        @DisplayName("TC-043: 게시물 상태 변경 시 이벤트 발행")
//        void publishEventOnStatusChange() {
//            // given
//            Post draftPost = createTestPost(PostStatus.DRAFT);
//            UpdatePostCommand statusCommand = UpdatePostCommand.publish(1L, 1L, 1L, 1L);
//            given(postRepository.findById(1L)).willReturn(draftPost);
//            given(postRepository.save(any(Post.class))).willReturn(draftPost);
//
//            // when
//            postUseCase.updatePost(statusCommand);
//
//            // then
//            verify(eventPublisher).publishEvent(any(PostStatusChangedEvent.class));
//        }
//    }
//
//    // Helper Methods
//    private Post createTestPost() {
//        return createTestPost(PostStatus.DRAFT);
//    }
//
//    private Post createTestPost(PostStatus status) {
//        return new Post(
//                1L, 1L, 1L, 1L, "테스트 제목", "테스트 내용", "테스트 요약",
//                status, LocalDateTime.now(), 0, 0, 0,
//                false, status == PostStatus.PUBLISHED ? LocalDateTime.now() : null
//        );
//    }
//}