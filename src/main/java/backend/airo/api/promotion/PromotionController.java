package backend.airo.api.promotion;

import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.global.dto.Response;
import backend.airo.api.global.swagger.PromotionControllerSwagger;
import backend.airo.api.promotion.dto.PromotionResponse;
import backend.airo.api.promotion.dto.PromotionImageResponse;
import backend.airo.api.promotion.dto.PromotionImageStatusResponse;
import backend.airo.application.promotion.usecase.PromotionUsecase;
import backend.airo.domain.promotion.Promotion;
import backend.airo.domain.promotion.PromotionResult;
import backend.airo.domain.promotion.PromotionImageResult;
import backend.airo.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;  // 추가
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;  // 추가
import java.util.concurrent.CompletableFuture;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/promotions")
@RequiredArgsConstructor
public class PromotionController implements PromotionControllerSwagger {

    private final PromotionUsecase promotionUsecase;

    @Override
    @PostMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    public Response<PromotionResponse> createPromotion(
            @PathVariable @Positive Long postId,
            @UserPrincipal User user) {

        log.info("홍보물 생성 요청: postId={}, userId={}", postId, user.getId());

        CompletableFuture<PromotionResult> future = promotionUsecase.generatePromotion(postId, user.getId());
        PromotionResult result = future.join(); // 동기 대기

        // PromotionResult를 Promotion으로 변환 (임시)
        Promotion promotion = new backend.airo.domain.promotion.Promotion(
                null, postId,
                result.spotName(), result.mainImageUrl(),
                result.recommendedTags(), result.emotions(),
                result.suggestedTitle(), result.content()
        );

        PromotionResponse response = PromotionResponse.fromDomain(promotion);

        log.info("홍보물 생성 완료: postId={}, spotName={}", postId, result.spotName());
        return Response.success(response);
    }

    @Override
    @PostMapping("/{postId}/image")
    @PreAuthorize("isAuthenticated()")
    public Response<PromotionImageResponse> createPromotionWithImage(
            @PathVariable @Positive Long postId,
            @UserPrincipal User user) {

        log.info("홍보물 이미지 생성 요청: postId={}, userId={}", postId, user.getId());

        CompletableFuture<PromotionImageResult> future = promotionUsecase.generatePromotionWithImage(postId, user.getId());
        PromotionImageResult result = future.join(); // 동기 대기

        PromotionImageResponse response = PromotionImageResponse.fromDomain(result);

        log.info("홍보물 이미지 생성 요청 완료: postId={}, taskId={}", postId, result.taskId());
        return Response.success(response);
    }

    @Override
    @GetMapping("/{postId}")
    public Response<PromotionResponse> getPromotion(
            @PathVariable @Positive Long postId,
            @UserPrincipal User user) {

        log.debug("홍보물 조회 요청: postId={}, userId={}", postId, user.getId());

        Promotion promotion = promotionUsecase.getPromotion(postId, user.getId());

        PromotionResponse response = PromotionResponse.fromDomain(promotion);
        return Response.success(response);
    }

    @Override
    @GetMapping("/status/{taskId}")
    public Response<PromotionImageStatusResponse> getPromotionImageStatus(
            @PathVariable String taskId,
            @UserPrincipal User user) {

        log.debug("홍보물 이미지 상태 조회: taskId={} ", taskId);

        PromotionImageResult result = promotionUsecase.getPromotionStatusWithImage(taskId);

        PromotionImageStatusResponse response = PromotionImageStatusResponse.fromDomain(result);
        return Response.success(response);
    }

    @Override
    @PutMapping("/{postId}/regenerate")
    @PreAuthorize("isAuthenticated()")
    public Response<PromotionImageResponse> regeneratePromotionWithImage(
            @PathVariable @Positive Long postId,
            @UserPrincipal User user) {

        log.info("홍보물 재생성 요청: postId={}, userId={}", postId, user.getId());

        CompletableFuture<PromotionImageResult> future = promotionUsecase.regeneratePromotionWithImage(postId, user.getId());
        PromotionImageResult result = future.join(); // 동기 대기

        PromotionImageResponse response = PromotionImageResponse.fromDomain(result);

        log.info("홍보물 재생성 요청 완료: postId={}, taskId={}", postId, result.taskId());
        return Response.success(response);
    }

    @Override
    @DeleteMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    public Response<Void> deletePromotion(
            @PathVariable @Positive Long postId,
            @UserPrincipal User user) {

        log.info("홍보물 삭제 요청: postId={}, userId={}", postId, user.getId());

        promotionUsecase.deletePromotion(postId, user.getId());

        log.info("홍보물 삭제 완료: postId={}", postId);
        return Response.success("홍보물 삭제 성공");
    }



    @Override
    @GetMapping("/{postId}/image")
    public ResponseEntity<byte[]> getPromotionImage(
            @PathVariable @Positive Long postId,
            @UserPrincipal User user) {

        try {
            log.debug("홍보물 이미지 조회: postId={}, userId={}", postId, user.getId());

            // 홍보물 존재 여부 확인 (캐시 적용)
            Promotion promotion = promotionUsecase.getPromotion(postId, user.getId());
            if (promotion == null) {
                log.warn("홍보물이 존재하지 않음: postId={}", postId);
                return ResponseEntity.notFound().build();
            }

            // 이미지 데이터 조회 (캐시 적용)
            byte[] imageData = promotionUsecase.getPromotionImageData(postId);
            if (imageData == null || imageData.length == 0) {
                log.warn("홍보물 이미지가 존재하지 않음: postId={}", postId);
                return ResponseEntity.notFound().build();
            }

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(imageData.length);
            headers.setCacheControl(CacheControl.maxAge(Duration.ofHours(24)));
            headers.add("Content-Disposition", "inline; filename=\"promotion_" + postId + ".png\"");

            log.info("홍보물 이미지 조회 성공: postId={}, size={} bytes", postId, imageData.length);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageData);

        } catch (Exception e) {
            log.error("홍보물 이미지 조회 실패: postId={}", postId, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}