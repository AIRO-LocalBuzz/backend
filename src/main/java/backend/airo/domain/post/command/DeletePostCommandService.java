package backend.airo.domain.post.command;

import backend.airo.domain.post.exception.PostException;
import backend.airo.domain.post.repository.PostRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeletePostCommandService {
    private final PostRepository postRepository;

    public void handle(
            @NotNull @Positive Long postId
    ) {
        log.info("게시물 삭제 요청 - postId: {}", postId);

        if (!postRepository.existsById(postId)) {
            throw PostException.notFound(postId);
        }

        postRepository.deleteById(postId);
        log.info("게시물 삭제 완료 - postId: {}", postId);
    }
}