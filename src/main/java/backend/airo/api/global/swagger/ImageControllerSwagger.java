package backend.airo.api.global.swagger;
import backend.airo.api.annotation.UserPrincipal;
import backend.airo.api.global.dto.Response;
import backend.airo.api.image.dto.ImageCreateRequest;
import backend.airo.api.image.dto.ImageReorderRequest;
import backend.airo.api.image.dto.ImageResponse;
import backend.airo.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
            @ApiResponse(
                    responseCode = "201",
                    description = "이미지 업로드 성공",
                    content = @Content(schema = @Schema(implementation = ImageResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400, 401",
                    description = "잘못된 요청/ 권한 없음",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
    })
    @PostMapping
    Response<ImageResponse> uploadSingleImage(
            @UserPrincipal User user,
            @RequestBody @Valid ImageCreateRequest request
    );


//------------------------------------------------------------------------------------------------------------------



    @Operation(summary = "다중 이미지 업로드", description = "여러 이미지를 한 번에 업로드합니다")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "이미지 업로드 성공",
                    content =  @Content(array = @ArraySchema(schema = @Schema(implementation = ImageResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "400, 401",
                    description = "잘못된 요청/ 권한 없음",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
    })
    @PostMapping("/bulk")
    Response<List<ImageResponse>> uploadMultipleImages(
            @UserPrincipal User user,
            @RequestBody List<ImageCreateRequest> requests
            );


//-------------------------------------------------------------------------------------------------------------------



    @Operation(summary = "이미지 상세 조회", description = "이미지 ID로 상세 정보를 조회합니다")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ImageResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없음")
    })
    @GetMapping("/{imageId}")
    Response<ImageResponse> getImage(@PathVariable @Min(1) Long imageId);


//-------------------------------------------------------------------------------------------------------------------



    @Operation(summary = "게시물별 이미지 목록 조회", description = "특정 게시물에 등록된 이미지 목록을 조회합니다")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ImageResponse.class)))
            ),
            @ApiResponse(responseCode = "404", description = "게시물을 찾을 수 없음")
    })
    @GetMapping("/posts/{postId}")
    Response<List<ImageResponse>> getImagesByPost(@PathVariable Long postId);


//-------------------------------------------------------------------------------------------------------------------



    @Operation(summary = "이미지 목록 조회 (페이징)", description = "페이징을 적용하여 이미지 목록을 조회합니다")
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(implementation = Page.class))
    )
    @GetMapping
    Response<Page<ImageResponse>> getImages(
            @Parameter(description = "페이지 정보") Pageable pageable
    );


//-------------------------------------------------------------------------------------------------------------------



    @Operation(summary = "이미지 순서 재정렬", description = "여러 이미지의 순서를 재정렬합니다")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "재정렬 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ImageResponse.class)))
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "권한 없음")
    })
    @PutMapping("/reorder")
    Response<List<ImageResponse>> reorderImages(
            @UserPrincipal User user,
            @RequestBody ImageReorderRequest request
    );


//-------------------------------------------------------------------------------------------------------------------



    @Operation(summary = "이미지 삭제", description = "이미지를 삭제합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없음"),
            @ApiResponse(responseCode = "401", description = "권한 없음")
    })
    @DeleteMapping("/{imageId}")
    Response<Void> deleteImage(
            @UserPrincipal User user,
            @PathVariable Long imageId
    );


//-------------------------------------------------------------------------------------------------------------------




    @Operation(summary = "다중 이미지 삭제", description = "여러 이미지를 한 번에 삭제합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "권한 없음")
    })
    @DeleteMapping
    Response<Void> deleteMultipleImages(
            @UserPrincipal User user,
            @RequestParam List<Long> imageIds
    );


//-------------------------------------------------------------------------------------------------------------------



    @Operation(summary = "내 이미지 목록 조회", description = "사용자별 이미지 목록을 페이징 처리하여 조회합니다")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = Page.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/my")
    Response<Page<ImageResponse>> getMyImages(
            @UserPrincipal User user,
            @Parameter(description = "페이지 정보") Pageable pageable
    );


//-------------------------------------------------------------------------------------------------------------------



    @Operation(summary = "내 게시물 이미지 목록 조회", description = "내 특정 게시물에 등록된 이미지 목록을 조회합니다")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ImageResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "401 | 404",
                    description = "권한 없음 | 게시물을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = Response.class))),
    })
    @GetMapping("/my/posts/{postId}")
    Response<List<ImageResponse>> getMyImagesByPost(
            @UserPrincipal User user,
            @PathVariable Long postId
    );
}
