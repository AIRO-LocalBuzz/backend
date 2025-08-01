package backend.airo.domain.post.command;

import backend.airo.api.image.dto.ImageCreateRequest;
import backend.airo.api.post.dto.PostCreateRequest;
import backend.airo.application.image.usecase.ImageCreateUseCase;
import backend.airo.domain.image.Image;
import backend.airo.domain.post.Post;
import backend.airo.domain.post.enums.PostStatus;
import backend.airo.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class CreatePostCommandService {

    private final PostRepository postRepository;
    private final ImageCreateUseCase imageCreateUseCase;

    public Post handle(PostCreateRequest request) {
        log.info("게시물 생성 시작: title={}, userId={}, status={}",
                request.title(), request.userId(), request.status());

        // 도메인 객체 생성
        Post post = createPostFromCommand(request);

        log.debug("생성된 Post 도메인 객체: {}", post);

        // 게시물 저장
        Post savedPost = postRepository.save(post);
        log.info("게시물 저장 완료: id={}, title={}", savedPost.getId(), savedPost.getTitle());

        processPostImages(savedPost.getId(), request);

        return savedPost;
    }


    private void processPostImages(Long postId, PostCreateRequest request) {
        List<ImageCreateRequest> imageRequests = request.images();
        Long userId = request.userId();

        log.debug("이미지 요청 리스트 개수: {}, userId: {}", imageRequests.size(), userId);

        List<Image> images = new java.util.ArrayList<>();

        for (int i = 0; i < imageRequests.size(); i++) {
            ImageCreateRequest imageRequest = imageRequests.get(i);
            Image image = new Image(userId, postId, imageRequest.imageUrl(), imageRequest.mimeType(), i+1);
            log.debug("처리중인 Image: postId={}, imageUrl={}, sortOrder={}", image.getPostId(), image.getImageUrl(), i+1);
            images.add(image);
        }

        List<Image> savedImages = imageCreateUseCase.uploadMultipleImages(images);
        log.debug("게시물 이미지 저장 완료: postId={}, 저장된 이미지 개수={}", postId, savedImages.size());
    }



    private Post createPostFromCommand(PostCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime publishedAt = (request.status() == PostStatus.PUBLISHED) ? now : null;

        Post post = new Post(
                null, // ID는 저장 시 생성
                request.userId(),
                request.title(),
                request.content(),
                null, // summary는 나중에 AI로 생성
                request.status(),
                request.withWhoTag(),
                request.forWhatTag(),
                request.emotionTags(),
                request.category(),
                request.travelDate(),
                request.location(),
                request.adress(),
                0, // 초기 조회수
                0, // 초기 좋아요 수
                0, // 초기 댓글 수
                request.isFeatured(),
                publishedAt
        );

        log.debug("createPostFromCommand 결과 Post: {}", post);
        return post;
    }
}