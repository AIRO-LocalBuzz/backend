package backend.airo.domain.post.command;

import backend.airo.api.image.dto.ImageCreateRequest;
import backend.airo.api.post.dto.PostCreateRequest;
import backend.airo.application.image.usecase.ImageCreateUseCase;
import backend.airo.application.thumbnail.ThumbnailGenerationService;
import backend.airo.domain.image.Image;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.exception.PostErrorCode;
import backend.airo.domain.post.exception.PostValidationException;
import backend.airo.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreatePostCommandService {

    private final PostRepository postRepository;
    private final ImageCreateUseCase imageCreateUseCase;
    private final ThumbnailGenerationService thumbnailGenerationService;

    @Transactional
    public Post handle(PostCreateRequest request, Long userId) {
        validateRequest(request, userId);

        log.info("게시물 생성 시작: title={}, userId={}, status={}",
                request.title(), userId, request.status());

        Post post = createPost(request, userId);
        Post savedPost = postRepository.save(post);

        processImages(request.images(), userId, savedPost.getId());

        log.info("게시물 저장 완료: id={}, title={}", savedPost.getId(), savedPost.getTitle());
        return savedPost;
    }

    @Transactional
    public Post handleWithThumbnail(PostCreateRequest request, Long userId) {
        validateRequest(request, userId);

        Post savedPost = handle(request, userId);
        thumbnailGenerationService.generateThumbnailAsync(savedPost);

        return savedPost;
    }

    private void validateRequest(PostCreateRequest request, Long userId) {
        if (request == null) {
            throw new PostValidationException(PostErrorCode.POST_REQUEST_REQUIRED, "request", "null");
        }
        if (userId == null || userId <= 0) {
            throw new PostValidationException(PostErrorCode.USER_ID_REQUIRED, "userId", String.valueOf(userId));
        }
        if (request.title() == null || request.title().trim().isEmpty()) {
            throw new PostValidationException(PostErrorCode.POST_TITLE_REQUIRED, "title", request.title());
        }
        if (request.content() == null || request.content().trim().isEmpty()) {
            throw new PostValidationException(PostErrorCode.POST_CONTENT_REQUIRED, "content", request.content());
        }
        if (request.status() == null) {
            throw new PostValidationException(PostErrorCode.POST_STATUS_REQUIRED, "status", "null");
        }
    }

    private void processImages(List<ImageCreateRequest> imageRequests, Long userId, Long postId) {
        if (imageRequests == null || imageRequests.isEmpty()) {
            return;
        }

        List<Image> images = IntStream.range(0, imageRequests.size())
                .mapToObj(i -> createImage(imageRequests.get(i), userId, postId, i + 1))
                .toList();

        List<Image> savedImages = imageCreateUseCase.uploadMultipleImages(images);
        log.debug("이미지 저장 완료: postId={}, 저장된 이미지 개수={}", postId, savedImages.size());
    }

    private Image createImage(ImageCreateRequest request, Long userId, Long postId, int sortOrder) {
        return new Image(userId, postId, request.imageUrl(), request.mimeType(), sortOrder);
    }

    private Post createPost(PostCreateRequest request, Long userId) {
        return new Post(
                null,
                userId,
                request.title(),
                request.content(),
                null, // AI로 생성될 요약
                request.status(),
                request.withWhoTag(),
                request.forWhatTag(),
                request.emotionTags(),
                request.category(),
                request.travelDate(),
                null, // 발행일은 별도 로직으로 처리
                request.adress(),
                0, // 초기 조회수
                0, // 초기 좋아요 수
                0, // 초기 댓글 수
                request.isFeatured(),
                LocalDateTime.now()
        );
    }
}