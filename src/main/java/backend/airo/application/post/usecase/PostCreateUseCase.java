package backend.airo.application.post.usecase;

import backend.airo.api.post.dto.PostCreateRequest;
import backend.airo.domain.point.command.UpsertPointCommand;
import backend.airo.domain.point_history.command.CreatePointHistoryCommand;
import backend.airo.domain.point_history.vo.PointType;
import backend.airo.domain.post.command.CreatePostCommandService;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static backend.airo.domain.post.exception.PostErrorCode.POST_ALREADY_PUBLISHED;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostCreateUseCase {

    private final CreatePostCommandService createPostCommandService;
    private final UpsertPointCommand upsertPointCommand;
    private final CreatePointHistoryCommand createPointHistoryCommand;


    @Transactional
    public Post createPost(PostCreateRequest request, Long userId) {

        if (request.status() == PostStatus.PUBLISHED && !request.canPublish()) {
            throw new PostPublishException(null, "발행에 필요한 필수 정보가 누락되었습니다 (카테고리, 위치)", POST_ALREADY_PUBLISHED);
        }

        Post savedPost = createPostCommandService.handle(request, userId);

        boolean handle = createPointHistoryCommand.handle(userId, 100L, savedPost.getId(), PointType.REPORT);
        if (handle) {
            upsertPointCommand.handle(userId, 100L);
        }
        return savedPost;
    }


}