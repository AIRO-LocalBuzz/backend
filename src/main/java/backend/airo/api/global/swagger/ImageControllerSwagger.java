package backend.airo.api.global.swagger;


import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.image.dto.ImageCreateRequest;
import backend.airo.api.image.dto.ImageReorderRequest;
import backend.airo.api.image.dto.ImageResponse;
import backend.airo.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Image", description = "이미지 관리 API")
@SecurityRequirement(name = "BearerAuth")
public interface ImageControllerSwagger {

    @Operation(summary = "단일 이미지 업로드", description = "새로운 이미지를 업로드합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "이미지 업로드 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "413", description = "파일 크기 초과")
    })
    @PostMapping
    ResponseEntity<ImageResponse> uploadSingleImage(
            @UserPrincipal User user,
            @RequestBody ImageCreateRequest request
    );


    @Operation(summary = "다중 이미지 업로드", description = "여러 이미지를 한 번에 업로드합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "이미지 업로드 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/bulk")
    ResponseEntity<List<ImageResponse>> uploadMultipleImages(
            @UserPrincipal User user,
            @RequestBody List<ImageCreateRequest> requests
            );



    @Operation(summary = "이미지 상세 조회", description = "이미지 ID로 상세 정보를 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없음")
    })
    @GetMapping("/{imageId}")
    ResponseEntity<ImageResponse> getImage(@PathVariable Long imageId);


    @Operation(summary = "게시물별 이미지 목록 조회", description = "특정 게시물의 모든 이미지를 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시물을 찾을 수 없음")
    })
    @GetMapping("/post/{postId}")
    ResponseEntity<List<ImageResponse>> getImagesByPost(@PathVariable Long postId);


    @Operation(summary = "이미지 목록 조회 (페이징)", description = "페이징을 적용하여 이미지 목록을 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<Page<ImageResponse>> getImages(
            @Parameter(description = "페이지 정보") Pageable pageable
    );


    @Operation(summary = "이미지 순서 재정렬", description = "여러 이미지의 순서를 재정렬합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재정렬 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @PutMapping("/reorder")
    ResponseEntity<List<ImageResponse>> reorderImages(
            @UserPrincipal User user,
            @RequestBody ImageReorderRequest request
            );


    @Operation(summary = "이미지 삭제", description = "이미지를 삭제합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @DeleteMapping("/{imageId}")
    ResponseEntity<Void> deleteImage(
            @UserPrincipal User user,
            @PathVariable Long imageId
            );


    @Operation(summary = "다중 이미지 삭제", description = "여러 이미지를 한 번에 삭제합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @DeleteMapping
    ResponseEntity<Void> deleteMultipleImages(
            @UserPrincipal User user,
            @RequestParam List<Long> imageIds
    );
}
