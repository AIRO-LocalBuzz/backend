package backend.airo.domain.post.command;

import backend.airo.api.post.dto.PostUpdateRequest;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.exception.PostException;
import backend.airo.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static backend.airo.domain.post.Post.updatePostFromCommand;
import static backend.airo.domain.post.exception.PostErrorCode.POST_CANNOT_CHANGE_STATUS;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpdatePostCommandService{

    private final PostRepository postRepository;

    public Post handle(PostUpdateRequest request, Post existingPost){

        if (!request.hasChanges()) {
            return existingPost;
        }

        if (request.isStatusChange()) {
            validateStatusChange(existingPost, request.status());
        }

        Post updatedPost = updatePostFromCommand(existingPost, request);
        Post savedPost = postRepository.save(updatedPost);

        log.info("게시물 수정 완료: id={}", savedPost.id());
        return savedPost;
    }


    private void validateStatusChange(Post post, PostStatus newStatus) {
        if (!isValidStatusTransition(post.status(), newStatus)) {
            throw PostException.statusChange(post.id(), post.status(), newStatus, POST_CANNOT_CHANGE_STATUS);
        }
    }


    private boolean isValidStatusTransition(PostStatus currentStatus, PostStatus newStatus) {
        return switch (currentStatus) {
            case DRAFT -> newStatus == PostStatus.PUBLISHED || newStatus == PostStatus.DRAFT;
            case PUBLISHED -> newStatus == PostStatus.ARCHIVED || newStatus == PostStatus.DRAFT || newStatus == PostStatus.PUBLISHED;
            case ARCHIVED -> newStatus == PostStatus.PUBLISHED || newStatus == PostStatus.DRAFT || newStatus == PostStatus.ARCHIVED;
        };
    }


}
