package backend.airo.api.image;

import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.global.swagger.ImageControllerSwagger;
import backend.airo.api.image.dto.ImageCreateRequest;
import backend.airo.api.image.dto.ImageReorderRequest;
import backend.airo.api.image.dto.ImageResponse;
import backend.airo.application.image.usecase.ImageCreateUseCase;
import backend.airo.application.image.usecase.ImageDeleteUseCase;
import backend.airo.application.image.usecase.ImageReadUseCase;
import backend.airo.application.image.usecase.ImageUpdateUseCase;
import backend.airo.domain.image.Image;
import backend.airo.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/images")
@RequiredArgsConstructor
public class ImageController implements ImageControllerSwagger {

    private final ImageReadUseCase imageReadUseCase;
    private final ImageCreateUseCase imageCreateUseCase;
    private final ImageUpdateUseCase imageUpdateUseCase;
    private final ImageDeleteUseCase imageDeleteUseCase;

    @Override
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ImageResponse> uploadSingleImage(
            @UserPrincipal User user,
            @RequestBody ImageCreateRequest request) {

        log.info("단일 이미지 업로드 요청 - 사용자 ID: {}, 이미지 URL: {}", user.getId(), request.imageUrl());

        Image image = request.toImage(user.getId());

        ImageResponse response = ImageResponse.from(image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Override
    @PostMapping("/bulk")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ImageResponse>> uploadMultipleImages(
            @UserPrincipal User user,
            @RequestBody List<ImageCreateRequest> requests
            ) {
        log.info("다중 이미지 업로드 요청 - 사용자 ID: {}, 이미지 개수: {}", user.getId(), requests.size());

        List<Image> images = requests.stream()
                .map(request -> request.toImage(user.getId()))
                .toList();

        List<Image> uploadedImages = imageCreateUseCase.uploadMultipleImages(images);
        List<ImageResponse> responses = uploadedImages.stream()
                .map(ImageResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }



    @Override
    @GetMapping("/{imageId}")
    public ResponseEntity<ImageResponse> getImage(@PathVariable Long imageId) {
        log.info("이미지 조회 요청 - 이미지 ID: {}", imageId);

        Image image = imageReadUseCase.getSingleImage(imageId);
        ImageResponse response = ImageResponse.from(image);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/posts/{postId}")
    public ResponseEntity<List<ImageResponse>> getImagesByPost(@PathVariable Long postId) {
        log.info("게시물별 이미지 목록 조회 요청 - 게시물 ID: {}", postId);

        List<Image> images = imageReadUseCase.getSortedImagesByPost(postId);
        List<ImageResponse> responses = images.stream()
                .map(ImageResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<ImageResponse>> getImages(Pageable pageable) {
        log.info("이미지 목록 조회 요청 - 페이지: {}, 크기: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Image> images = imageReadUseCase.getPagedImages(pageable);
        Page<ImageResponse> responses = images.map(ImageResponse::from);

        return ResponseEntity.ok(responses);
    }



    @Override
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/reorder")
    public ResponseEntity<List<ImageResponse>> reorderImages(
            @UserPrincipal User user,
            @RequestBody ImageReorderRequest request
    ) {
        log.info("이미지 순서 재정렬 요청 - 사용자 ID: {}, 이미지 개수: {}", user.getId(), request.imageIds().size());

        List<Image> reorderedImages = imageUpdateUseCase.reorderImages(request.imageIds());
        List<ImageResponse> responses = reorderedImages.stream()
                .map(ImageResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }




    @Override
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(
            @UserPrincipal User user,
            @PathVariable Long imageId) {
        log.info("이미지 삭제 요청 - 사용자 ID: {}, 이미지 ID: {}", user.getId(), imageId);

        imageDeleteUseCase.deleteImageWithAuth(imageId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping
    public ResponseEntity<Void> deleteMultipleImages(
            @UserPrincipal User user,
            @RequestParam List<Long> imageIds
    ) {
        log.info("다중 이미지 삭제 요청 - 사용자 ID: {}, 이미지 개수: {}", user.getId(), imageIds.size());

        imageDeleteUseCase.deleteMultipleImages(imageIds);
        return ResponseEntity.noContent().build();
    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my")
    public ResponseEntity<Page<ImageResponse>> getMyImages(
            @UserPrincipal User user,
            Pageable pageable
    ) {
        log.info("내 이미지 목록 조회 요청 - 사용자 ID: {}", user.getId());

        // 사용자별 이미지 조회 로직 (UseCase에 추가 필요)
        Page<Image> images = imageReadUseCase.getPagedImages(pageable);
        // TODO: 실제로는 사용자별 필터링이 필요
        Page<ImageResponse> responses = images.map(ImageResponse::from);

        return ResponseEntity.ok(responses);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my/posts/{postId}")
    public ResponseEntity<List<ImageResponse>> getMyImagesByPost(
            @UserPrincipal User user,
            @PathVariable Long postId
    ) {
        log.info("내 게시물 이미지 목록 조회 요청 - 사용자 ID: {}, 게시물 ID: {}", user.getId(), postId);

        List<Image> images = imageReadUseCase.getSortedImagesByPost(postId);

        List<ImageResponse> responses = images.stream()
                .map(ImageResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }
}