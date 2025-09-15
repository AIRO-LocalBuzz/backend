package backend.airo.domain.post.command;

import backend.airo.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpdatePostViewCountCommand {

    private final PostRepository postRepository;

    public void handle(Long postId) {
        postRepository.upsertPostViewCount(postId);
    }

}
