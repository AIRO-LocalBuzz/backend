package backend.airo.domain.post.command;

import backend.airo.api.image.dto.ImageCreateRequest;
import backend.airo.api.post.dto.PostCreateRequest;
import backend.airo.application.image.usecase.ImageUseCase;
import backend.airo.application.promotion.PromotionGenerationService;
import backend.airo.domain.image.Image;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.IntStream;

import static backend.airo.domain.image.Image.createImage;
import static backend.airo.domain.post.Post.createPost;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreatePostCommandService {

    private final PostRepository postRepository;
    private final ImageUseCase imageUseCase;
    private final PromotionGenerationService thumbnailGenerationService;

    @Transactional
    public Post handle(PostCreateRequest request, Long userId) {
        log.info("게시물 생성 시작: title={}, userId={}, status={}",
                request.title(), userId, request.status());

        Post post = createPost(request, userId);
        Post savedPost = postRepository.save(post);

        processImages(request.images(), userId, savedPost.getId());

        log.info("게시물 저장 완료: id={}, title={}", savedPost.getId(), savedPost.getTitle());
        return savedPost;
    }



    private void processImages(List<ImageCreateRequest> imageRequests, Long userId, Long postId) {

        List<Image> images = IntStream.range(0, imageRequests.size())
                .mapToObj(i -> createImage(imageRequests.get(i), userId, postId, i + 1))
                .toList();

        List<Image> savedImages = imageUseCase.uploadMultipleImages(images);
        log.debug("이미지 저장 완료: postId={}, 저장된 이미지 개수={}", postId, savedImages.size());
    }


}