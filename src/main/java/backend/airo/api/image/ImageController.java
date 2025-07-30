package backend.airo.api.image;

import backend.airo.api.annotation.UserPrincipal;
import backend.airo.application.image.usecase.ImageUseCase;
import backend.airo.common.jwt.JwtAuthenticationToken;
import backend.airo.domain.image.Image;
import backend.airo.api.image.dto.*;
import backend.airo.api.global.swagger.ImageControllerSwagger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import backend.airo.domain.user.User;

@Slf4j
@RestController
@RequestMapping("/v1/images")
@RequiredArgsConstructor
public class ImageController implements ImageControllerSwagger {

    private final ImageUseCase imageUseCase;

    @Override
    @PostMapping
    public ResponseEntity<ImageResponse> uploadSingleImage(
            @UserPrincipal User user,
            @RequestBody ImageCreateRequest request) {

        log.info("단일 이미지 업로드 요청 - 사용자 ID: {}, 이미지 URL: {}", user.getId(), request.imageUrl());

        Image image = request.toImage(user.getId());

        ImageResponse response = ImageResponse.from(image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/bulk")
    @Override
    public ResponseEntity<List<ImageResponse>> uploadMultipleImages(
            @UserPrincipal User user,
            @RequestBody List<ImageCreateRequest> requests
            ) {
        log.info("다중 이미지 업로드 요청 - 사용자 ID: {}, 이미지 개수: {}", user.getId(), requests.size());

        List<Image> images = requests.stream()
                .map(request -> request.toImage(user.getId()))
                .toList();

        List<Image> uploadedImages = imageUseCase.uploadMultipleImages(images);
        List<ImageResponse> responses = uploadedImages.stream()
                .map(ImageResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }



    @Override
    @GetMapping("/{imageId}")
    public ResponseEntity<ImageResponse> getImage(@PathVariable Long imageId) {
        log.info("이미지 조회 요청 - 이미지 ID: {}", imageId);

        Image image = imageUseCase.getSingleImage(imageId);
        ImageResponse response = ImageResponse.from(image);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/posts/{postId}")
    public ResponseEntity<List<ImageResponse>> getImagesByPost(@PathVariable Long postId) {
        log.info("게시물별 이미지 목록 조회 요청 - 게시물 ID: {}", postId);

        List<Image> images = imageUseCase.getSortedImagesByPost(postId);
        List<ImageResponse> responses = images.stream()
                .map(ImageResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<ImageResponse>> getImages(Pageable pageable) {
        log.info("이미지 목록 조회 요청 - 페이지: {}, 크기: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Image> images = imageUseCase.getPagedImages(pageable);
        Page<ImageResponse> responses = images.map(ImageResponse::from);

        return ResponseEntity.ok(responses);
    }



    @PutMapping("/reorder")
    @Override
    public ResponseEntity<List<ImageResponse>> reorderImages(
            @UserPrincipal User user,
            @RequestBody ImageReorderRequest request
    ) {
        log.info("이미지 순서 재정렬 요청 - 사용자 ID: {}, 이미지 개수: {}", user.getId(), request.imageIds().size());

        // 권한 확인 (모든 이미지가 해당 사용자의 것인지 확인)
        for (Long imageId : request.imageIds()) {
            Image image = imageUseCase.getSingleImage(imageId);
            if (!image.getUserId().equals(user.getId())) {
                log.warn("이미지 재정렬 권한 없음 - 사용자 ID: {}, 이미지 ID: {}, 이미지 소유자 ID: {}",
                        user.getId(), imageId, image.getUserId());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        List<Image> reorderedImages = imageUseCase.reorderImages(request.imageIds());
        List<ImageResponse> responses = reorderedImages.stream()
                .map(ImageResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }




    @DeleteMapping("/{imageId}")
    @Override
    public ResponseEntity<Void> deleteImage(
            @UserPrincipal User user,
            @PathVariable Long imageId) {
        log.info("이미지 삭제 요청 - 사용자 ID: {}, 이미지 ID: {}", user.getId(), imageId);

        imageUseCase.deleteImageWithAuth(imageId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Override
    public ResponseEntity<Void> deleteMultipleImages(
            @UserPrincipal User user,
            @RequestParam List<Long> imageIds
    ) {
        log.info("다중 이미지 삭제 요청 - 사용자 ID: {}, 이미지 개수: {}", user.getId(), imageIds.size());

        // 권한 확인
        for (Long imageId : imageIds) {
            Image image = imageUseCase.getSingleImage(imageId);
            if (!image.getUserId().equals(user.getId())) {
                log.warn("이미지 삭제 권한 없음 - 사용자 ID: {}, 이미지 ID: {}, 이미지 소유자 ID: {}",
                        user.getId(), imageId, image.getUserId());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        imageUseCase.deleteMultipleImages(imageIds);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/posts/{postId}/stats")
    public ResponseEntity<ImageStatsResponse> getImageStats(@PathVariable Long postId) {
        log.info("게시물 이미지 통계 조회 요청 - 게시물 ID: {}", postId);

        int imageCount = imageUseCase.getImageCountByPost(postId);
        long totalSize = imageUseCase.getTotalImageSizeByPost(postId);

        ImageStatsResponse response = ImageStatsResponse.builder()
                .postId(postId)
                .imageCount(imageCount)
                .totalSize(totalSize)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<ImageResponse>> getMyImages(
            @UserPrincipal User user,
            Pageable pageable
    ) {
        log.info("내 이미지 목록 조회 요청 - 사용자 ID: {}", user.getId());

        // 사용자별 이미지 조회 로직 (UseCase에 추가 필요)
        Page<Image> images = imageUseCase.getPagedImages(pageable);
        // TODO: 실제로는 사용자별 필터링이 필요
        Page<ImageResponse> responses = images.map(ImageResponse::from);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/my/posts/{postId}")
    public ResponseEntity<List<ImageResponse>> getMyImagesByPost(
            @UserPrincipal User user,
            @PathVariable Long postId
    ) {
        log.info("내 게시물 이미지 목록 조회 요청 - 사용자 ID: {}, 게시물 ID: {}", user.getId(), postId);

        List<Image> images = imageUseCase.getSortedImagesByPost(postId);

        // 권한 확인 (해당 게시물의 이미지들이 현재 사용자의 것인지 확인)
        List<Image> myImages = images.stream()
                .filter(image -> image.getUserId().equals(user.getId()))
                .toList();

        List<ImageResponse> responses = myImages.stream()
                .map(ImageResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }
}