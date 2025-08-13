// 파일: `src/main/java/backend/airo/domain/post/query/GetPostListQueryService.java`
package backend.airo.domain.post.query;

import backend.airo.api.post.dto.PostListRequest;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                    firstPost.getId(), firstPost.getTitle(), firstPost.getPublishedAt(), firstPost.getStatus());
        }
        return posts;
    }
}