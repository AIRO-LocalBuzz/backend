package backend.airo.application.post.usecase;

import backend.airo.domain.post.command.DeletePostLikeCommand;
import backend.airo.domain.post.command.PostDecrementLikeCountCommand;
import backend.airo.domain.post.command.PostIncrementLikeCountCommand;
import backend.airo.domain.post.command.PostLikeUpsertLikeCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostLikeUserCase {

    private final PostDecrementLikeCountCommand postDecrementLikeCountCommand;
    private final PostIncrementLikeCountCommand postIncrementLikeCountCommand;
    private final PostLikeUpsertLikeCommand postLikeUpsertLikeCommand;
    private final DeletePostLikeCommand deletePostLikeCommand;

    @Transactional
    public void upsertPostLike(Long postId, Long userId) {
        int postLike = postLikeUpsertLikeCommand.handle(postId, userId);
        System.out.println("postLike :: " + postLike);
        if (postLike == 1) {
            System.out.println("postIncrementLikeCountCommand 실행 됨.");
            int postIncrementLike = postIncrementLikeCountCommand.handle(postId);
            if (postIncrementLike != 1) {
                //예외 처리
                return;
            }
        }
    }

    @Transactional
    public void deletePostLike(Long postId, Long userId) {
        int deleted = deletePostLikeCommand.handle(postId, userId);
        if (deleted == 1) {
            int dec = postDecrementLikeCountCommand.handle(postId);
            if (dec != 1) {
                //예외 처리
                return;
            }
        }
    }
}
