//package backend.airo.application.thumbnail;
//
//import backend.airo.domain.post.event.PostCreatedEvent;
//import backend.airo.domain.thumbnail.LLMProvider;
//import backend.airo.domain.thumbnail.ThumbnailRequest;
//import backend.airo.domain.thumbnail.ThumbnailResult;
//import backend.airo.domain.image.repository.ImageRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.event.EventListener;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class ThumbnailGenerationService {
//
//    private final LLMProvider llmProvider;
//    private final ImageRepository imageRepository;
//
//    @Async
//    @EventListener
//    @Transactional
//    public void handlePostCreated(PostCreatedEvent event) {
//        try {
//            log.info("썸네일 생성 시작: postId={}", event.getPostId());
//
//            // 이미지 URL 조회
//            List<String> imageUrls = imageRepository.findImageUrlsByPostId(event.getPostId());
//
//            // 썸네일 요청 생성
//            ThumbnailRequest request = ThumbnailRequest.from(event.getPost(), imageUrls);
//
//            // LLM으로 썸네일 생성
//            ThumbnailResult result = llmProvider.generateThumbnail(request);
//
//            // TODO: Post에 썸네일 정보 업데이트
//            updatePostThumbnail(event.getPostId(), result);
//
//            log.info("썸네일 생성 완료: postId={}, mainImage={}",
//                    event.getPostId(), result.mainImageUrl());
//
//        } catch (Exception e) {
//            log.error("썸네일 생성 실패: postId={}", event.getPostId(), e);
//            // TODO: 재시도 로직 또는 알림
//        }
//    }
//
//    private void updatePostThumbnail(Long postId, ThumbnailResult result) {
//        // Post 엔티티 업데이트 로직
//        log.debug("Post 썸네일 업데이트: postId={}, result={}", postId, result);
//    }
//}
