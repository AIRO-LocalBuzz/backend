package backend.airo.application.thumbnail;

import backend.airo.domain.post.Post;
import backend.airo.domain.thumbnail.LLMProvider;
import backend.airo.domain.thumbnail.Thumbnail;
import backend.airo.domain.thumbnail.ThumbnailRequest;
import backend.airo.domain.thumbnail.ThumbnailResult;
import backend.airo.domain.image.repository.ImageRepository;
import backend.airo.domain.thumbnail.repository.ThumbnailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThumbnailGenerationService {

    private final LLMProvider llmProvider;
    private final ImageRepository imageRepository;
    private final ThumbnailRepository thumbnailRepository;

    public void generateThumbnailAsync(Post post) {
        CompletableFuture.runAsync(() -> {
            try {
                log.info("썸네일 생성 시작: postId={}", post.id());

                List<String> imageUrls = imageRepository.findImageUrlsByPostId(post.id());
                ThumbnailRequest request = ThumbnailRequest.from(post, imageUrls);
                ThumbnailResult result = llmProvider.generateThumbnail(request);

                saveThumbnail(post.id(), result);

                log.info("썸네일 생성 완료: postId={}", post.id());
            } catch (Exception e) {
                log.error("썸네일 생성 실패: postId={}", post.id(), e);
            }
        });
    }

    private void saveThumbnail(Long postId, ThumbnailResult result) {
        Thumbnail thumbnail = Thumbnail.create(postId, result);
        thumbnailRepository.save(thumbnail);
        log.debug("썸네일 저장 완료: postId={}, result={}", postId, result);
    }
}
