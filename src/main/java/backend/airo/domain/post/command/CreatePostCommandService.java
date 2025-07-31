package backend.airo.domain.post.command;

import backend.airo.api.post.dto.PostCreateRequest;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.exception.PostPublishException;
import backend.airo.domain.post.repository.PostRepository;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시물 생성 커맨드
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CreatePostCommandService{

    private final PostRepository postRepository;

    public Post handle(PostCreateRequest request){
        log.info("게시물 생성 시작: title={}, userId={}, status={}",
                request.title(), request.userId(), request.status());

        // 도메인 객체 생성
        Post post = createPostFromCommand(request);

        // 게시물 저장
        Post savedPost = postRepository.save(post);
        log.info("게시물 저장 완료: id={}, title={}", savedPost.getId(), savedPost.getTitle());

        return savedPost;

    }

    private Post createPostFromCommand(PostCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime publishedAt = (request.status() == PostStatus.PUBLISHED) ? now : null;

        return new Post(
                null, // ID는 저장 시 생성
                request.userId(),
                request.categoryId(),
                request.locationId(),
                request.title(),
                request.content(),
                null, // summary는 나중에 AI로 생성
                request.status(),
                request.travelDate(),
                0, // 초기 조회수
                0, // 초기 좋아요 수
                0, // 초기 댓글 수
                request.isFeatured(),
                publishedAt
        );
    }
}