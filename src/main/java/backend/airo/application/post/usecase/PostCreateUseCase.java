package backend.airo.application.post.usecase;

import backend.airo.api.image.dto.ImageCreateRequest;
import backend.airo.api.post.dto.PostCreateRequest;
import backend.airo.application.image.usecase.ImageUseCase;
import backend.airo.domain.image.Image;
import backend.airo.domain.image.command.CreateImageCommandService;
import backend.airo.domain.post.command.CreatePostCommandService;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.repository.PostRepository;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static backend.airo.domain.post.exception.PostErrorCode.POST_ALREADY_PUBLISHED;

/**
 * PostUseCase - 게시물 관련 비즈니스 로직 처리
 * Application Layer의 핵심 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostCreateUseCase {

    private final CreatePostCommandService createPostCommandService;
    private final CreateImageCommandService createImageCommandService;
    private final ImageUseCase imageUseCase;

    // ===== 게시물 생성 =====

    @Transactional
    public Post createPost(PostCreateRequest request) {

        // 발행 조건 검증
        if (request.status() == PostStatus.PUBLISHED && !request.canPublish()) {
            throw new PostPublishException(null, "발행에 필요한 필수 정보가 누락되었습니다 (카테고리, 위치)", POST_ALREADY_PUBLISHED);
        }

        Post savedPost = createPostCommandService.handle(request);



        return savedPost;
    }


    // ===== Private Helper Methods =====



}