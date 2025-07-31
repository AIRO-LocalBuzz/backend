package backend.airo.api.global.swagger;

import backend.airo.api.global.dto.PageResponse;
import backend.airo.api.global.dto.Response;
import backend.airo.api.shop.dto.ShopInfoResponse;
import backend.airo.api.shop.dto.ShopListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.RequestParam;

public interface ShopControllerSwagger {

    @Operation(summary = "각 지역별 소상공인 상점 조회", description = "각 지역별 소상공인 상점 조회 API")
    @Parameters({
            @Parameter(name = "megaCode", description = "도시 코드", example = "48"),
            @Parameter(name = "cityCode", description = "도시 지역구 코드", example = "48310"),
            @Parameter(name = "page", description = "시작 페이지 번호", example = "0"),
            @Parameter(name = "size", description = "페이지에 표시할 갯수", example = "20")
    })
    @ApiResponse(
            responseCode = "200",
            description = "소상공인 상점 조회 성공",
            content = @Content(schema = @Schema(implementation = ShopListResponse.class))
    )
    Response<PageResponse<ShopListResponse>>  getShoplList(
            @RequestParam() String megaCode,
            @RequestParam() String cityCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    );

    @Operation(summary = "소상공인 상점 정보 상세 조회", description = "소상공인 상점 정보 조회 API")
    @Parameters({
            @Parameter(name = "shopId", description = "상점 ID", example = "1")
    })
    @ApiResponse(
            responseCode = "200",
            description = "소상공인 상점 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = ShopInfoResponse.class))
    )
    Response<ShopInfoResponse> getShopInfo(@NotBlank(message = "시작 날짜는 필수 입니다.") String shopId);

}
