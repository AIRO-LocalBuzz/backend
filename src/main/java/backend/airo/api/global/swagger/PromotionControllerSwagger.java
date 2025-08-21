package backend.airo.api.global.swagger;

import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.global.dto.Response;
import backend.airo.api.promotion.dto.PromotionImageResponse;
import backend.airo.api.promotion.dto.PromotionImageStatusResponse;
import backend.airo.api.promotion.dto.PromotionResponse;
import backend.airo.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Promotion", description = "홍보물 관리 API")
@SecurityRequirement(name = "BearerAuth")
public interface PromotionControllerSwagger {

    @Operation(summary = "홍보물 생성 (텍스트만)",
            description = "게시물에 대한 텍스트 홍보물을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "홍보물 생성 요청 성공",
                    content = @Content(schema = @Schema(implementation = PromotionResponse.class))),
            @ApiResponse(responseCode = "400 | 401 | 404",
                    description = "잘못된 요청 | 권한 없음 | 게시물을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{postId}")
    Response<PromotionResponse> createPromotion(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable @Positive Long postId,
            @UserPrincipal User user);

    @Operation(summary = "홍보물 생성 (이미지 포함)",
            description = "게시물에 대한 텍스트 + 이미지 홍보물을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "홍보물 이미지 생성 요청 성공",
                    content = @Content(schema = @Schema(implementation = PromotionImageResponse.class))),
            @ApiResponse(responseCode = "400 | 401 | 404",
                    description = "잘못된 요청 | 권한 없음 | 게시물을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{postId}/image")
    Response<PromotionImageResponse> createPromotionWithImage(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable @Positive Long postId,
            @UserPrincipal User user);

    @Operation(summary = "홍보물 조회",
            description = "게시물의 홍보물을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "홍보물 조회 성공",
                    content = @Content(schema = @Schema(implementation = PromotionResponse.class))),
            @ApiResponse(responseCode = "401 | 404",
                    description = "권한 없음 | 홍보물을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/{postId}")
    Response<PromotionResponse> getPromotion(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable @Positive Long postId,
            @UserPrincipal User user);

    @Operation(summary = "홍보물 이미지 상태 조회",
            description = "홍보물 이미지 생성 진행 상태를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "상태 조회 성공",
                    content = @Content(schema = @Schema(implementation = PromotionImageStatusResponse.class))),
            @ApiResponse(responseCode = "401 | 404",
                    description = "권한 없음 | 작업을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/status/{taskId}")
    Response<PromotionImageStatusResponse> getPromotionImageStatus(
            @Parameter(description = "작업 ID", required = true)
            @PathVariable String taskId,
            @UserPrincipal User user);

    @Operation(summary = "홍보물 재생성",
            description = "기존 홍보물을 삭제하고 새로 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "홍보물 재생성 요청 성공",
                    content = @Content(schema = @Schema(implementation = PromotionImageResponse.class))),
            @ApiResponse(responseCode = "400 | 401 | 404",
                    description = "잘못된 요청 | 권한 없음 | 게시물을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{postId}/regenerate")
    Response<PromotionImageResponse> regeneratePromotionWithImage(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable @Positive Long postId,
            @UserPrincipal User user);

    @Operation(summary = "홍보물 삭제",
            description = "게시물의 홍보물을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "홍보물 삭제 성공"),
            @ApiResponse(responseCode = "401 | 404",
                    description = "권한 없음 | 홍보물을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{postId}")
    Response<Void> deletePromotion(
            @Parameter(description = "게시물 ID", required = true)
            @PathVariable @Positive Long postId,
            @UserPrincipal User user);




    @Operation(
            summary = "홍보물 이미지 조회",
            description = "생성된 홍보물 이미지를 PNG 형태로 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이미지 조회 성공",
                            content = @Content(mediaType = "image/png")),
                    @ApiResponse(responseCode = "404", description = "홍보물 또는 이미지가 존재하지 않음"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping("/{postId}/image")
    ResponseEntity<byte[]> getPromotionImage(
            @Parameter(description = "게시물 ID", required = true) 
            @PathVariable @Positive Long postId,  // @PathVariable @Positive 추가
            @Parameter(hidden = true) @UserPrincipal User user  // @UserPrincipal 추가
    );
}