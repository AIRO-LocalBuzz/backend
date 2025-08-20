// 파일: `src/main/java/backend/airo/domain/post/query/GetPostListQueryService.java`
package backend.airo.domain.post.query;

import backend.airo.api.post.dto.PostListRequest;
import backend.airo.api.post.dto.PostSliceRequest;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GetPostListQueryService {

    private final PostRepository postRepository;

    public Page<Post> handle(PostListRequest request) {
        Pageable pageable = buildPageable(request);
        return retrievePosts(request.sortBy(), pageable, request.status());
    }

    public Slice<Post> handleSlice(PostSliceRequest request) {
        log.debug("무한스크롤 게시물 조회: lastPostId={}, size={}",
                request.lastPostId(), request.size());

        // 커서 기반 페이징: lastPostId 이후의 데이터만 조회
        return postRepository.findSliceAfterCursor(request.lastPostId(), request.size());
    }


    private Pageable buildPageable(PostListRequest request) {
        Sort sort = determineSort(request.sortBy());
        return PageRequest.of(request.page(), request.size(), sort);
    }

    private Sort determineSort(String sortBy) {
        if (sortBy == null) {
            return Sort.by(Sort.Direction.DESC, "publishedAt");
        }
        switch (sortBy.toLowerCase()) {
            case "likecount":
                return Sort.by(Sort.Direction.DESC, "likeCount");
            case "viewcount":
                return Sort.by(Sort.Direction.DESC, "viewCount");
            default:
                return Sort.by(Sort.Direction.DESC, "publishedAt");
        }
    }

    private Page<Post> retrievePosts(String sortBy, Pageable pageable, PostStatus status) {
        String sortKey = sortBy != null ? sortBy.toLowerCase() : "";
        Page<Post> posts;
        switch (sortKey) {
            case "likecount":
                posts = postRepository.findAllOrderByLikeCountDesc(pageable);
                break;
            case "viewcount":
                posts = postRepository.findAllOrderByViewCountDesc(pageable);
                break;
            default:
                posts = postRepository.findByStatus(status, pageable);
                break;
        }
        log.info("조회 결과 - 총 개수: {}, 페이지 개수: {}, 현재 페이지 사이즈: {}",
                posts.getTotalElements(), posts.getTotalPages(), posts.getNumberOfElements());
        if (!posts.getContent().isEmpty()) {
            Post firstPost = posts.getContent().get(0);
            log.debug("첫 번째 Post - ID: {}, title: {}, publishedAt: {}, status: {}",
                    firstPost.id(), firstPost.title(), firstPost.publishedAt(), firstPost.status());
        }
        return posts;
    }
}