package backend.airo.domain.post.query;

import backend.airo.api.post.dto.PostSummaryResponse;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.repository.PostRepository;
import backend.airo.domain.thumbnail.Thumbnail;
import backend.airo.domain.thumbnail.repository.ThumbnailRepository;
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
    private final ThumbnailRepository thumbnailRepository;

    public Post handle(Long postId) {
        return postRepository.findById(postId);
    }

    public Thumbnail handleThumbnail(Long thumbnailId) {
        return thumbnailRepository.findById(thumbnailId);
    }

    public PostSummaryResponse handlePostSummary(Long postId) {
        log.debug("게시물 요약 조회 시작: postId={}", postId);
        return postRepository.findPostSummaryById(postId);
    }

    public Long getMaxPostId() {
        log.debug("DB에서 최대 PostID 조회");
        return postRepository.findMaxPostId();
    }

    public boolean existsPostWithIdLessThan(Long postId) {
        log.debug("DB에서 최소 PostID 조회");
        return postRepository.existsByIdLessThan(postId);
    }

    public boolean existsById(Long postId) {
        log.debug("게시물 존재 여부 확인: postId={}", postId);
        return postRepository.existsById(postId);
    }
}