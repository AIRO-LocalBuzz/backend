package backend.airo.domain.post.query;

import backend.airo.api.post.dto.PostListRequest;
import backend.airo.domain.image.repository.ImageRepository;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;



@RequiredArgsConstructor
@Component
public class GetPostListQueryService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    /**
     * 발행일 기준 최신순으로 게시물 목록 조회
     */
    public Page<Post> getRecentPosts(PostListRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("쿼리 정보는 필수입니다");
        }

        Pageable pageable = createPageableForPublishedAt(request);

        // 발행된 게시물만 조회 (publishedAt이 null이 아닌 것들)
        Page<Post> postPage = postRepository.findByStatusAndPublishedAtIsNotNullOrderByPublishedAtDesc(
                PostStatus.PUBLISHED,
                pageable
        );

        return postPage;
    }


    /**
     * publishedAt 기준으로 Pageable 생성 (항상 최신순)
     */
    private Pageable createPageableForPublishedAt(PostListRequest request) {
        // publishedAt 기준으로 항상 최신순 정렬
        Sort sort = Sort.by(Sort.Direction.ASC, "publishedAt");
        return PageRequest.of(request.page(), request.size(), sort);
    }

}