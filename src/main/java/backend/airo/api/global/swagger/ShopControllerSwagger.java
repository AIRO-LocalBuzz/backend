package backend.airo.api.global.swagger;

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

import java.util.List;

public interface ShopControllerSwagger {

    @Operation(summary = "각 지역별 소상공인 상점 조회", description = "각 지역별 소상공인 상점 조회 API")
    @Parameters({
            @Parameter(name = "megaNmae", description = "각 도시 이름", example = "서울특별시"),
            @Parameter(name = "cityName", description = "각 도시 지역구 이름", example = "마포구")
    })

    @ApiResponse(
            responseCode = "200",
            description = "소상공인 상점 조회 성공",
            content = @Content(schema = @Schema(implementation = ShopListResponse.class))
    )
    Response<List<ShopListResponse>> getShoplList(
            @RequestParam() String megaNmae,
            @RequestParam() String cityName
    );

    @Operation(summary = "소상공인 상점 정보 상세 조회", description = "소상공인 상점 정보 조회 API")
    @Parameters({
            @Parameter(name = "clutrFatvlId", description = "상점 ID", example = "1")
    })
    @ApiResponse(
            responseCode = "200",
            description = "소상공인 상점 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = ShopInfoResponse.class))
    )
    Response<ShopInfoResponse> getShopInfo(@NotBlank(message = "시작 날짜는 필수 입니다.") String shopId);

}
