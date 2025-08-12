package backend.airo.domain.post.command;

import backend.airo.api.post.dto.PostUpdateRequest;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.exception.PostException;
import backend.airo.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static backend.airo.domain.post.Post.updatePostFromCommand;
import static backend.airo.domain.post.exception.PostErrorCode.POST_CANNOT_CHANGE_STATUS;
import static backend.airo.domain.post.exception.PostErrorCode.POST_PUBLISH_INVALID_CONDITION;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpdatePostCommandService{

    private final PostRepository postRepository;

    public Post handle(PostUpdateRequest request, Post existingPost){

        // 수정 사항이 없으면 기존 게시물 반환
        if (!request.hasChanges()) {
            return existingPost;
        }

        // 상태 변경 검증
        if (request.isStatusChange()) {
            validateStatusChange(existingPost, request.status());
        }

        Post updatedPost = updatePostFromCommand(existingPost, request);
        Post savedPost = postRepository.save(updatedPost);

        log.info("게시물 수정 완료: id={}", savedPost.getId());
        return savedPost;
    }


    private void validateStatusChange(Post post, PostStatus newStatus) {
        if (!isValidStatusTransition(post.getStatus(), newStatus)) {
            throw PostException.statusChange(post.getId(), post.getStatus(), newStatus, POST_CANNOT_CHANGE_STATUS);
        }
    }


    private boolean isValidStatusTransition(PostStatus currentStatus, PostStatus newStatus) {
        return switch (currentStatus) {
            case DRAFT -> newStatus == PostStatus.PUBLISHED;
            case PUBLISHED -> newStatus == PostStatus.ARCHIVED || newStatus == PostStatus.DRAFT;
            case ARCHIVED -> newStatus == PostStatus.PUBLISHED || newStatus == PostStatus.DRAFT;
        };
    }


}
