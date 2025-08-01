package backend.airo.domain.post.query;

import backend.airo.domain.post.Post;
import backend.airo.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 게시물 단건 조회 쿼리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GetPostQueryService {
    private final PostRepository postRepository;

    public Post handle(Long postId) {
        return postRepository.findById(postId);
    }


}