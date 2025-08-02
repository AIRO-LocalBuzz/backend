package backend.airo.api.global.swagger;

import backend.airo.api.area_code.dto.CityCodeResponse;
import backend.airo.api.area_code.dto.MegaCodeResponse;
import backend.airo.api.global.dto.Response;
import backend.airo.api.shop.dto.ShopInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

public interface AreaCodeControllerSwagger {

    @Operation(summary = "도시 코드 조회", description = "도시 코드 조회 API")
    @ApiResponse(
            responseCode = "200",
            description = "도시 코드 조회 성공",
            content = @Content(schema = @Schema(implementation = ShopInfoResponse.class))
    )
    Response<List<MegaCodeResponse>> getMegaCodes();

    @Operation(summary = "지역 코드 조회", description = "지역 코드 조회 API")
    @ApiResponse(
            responseCode = "200",
            description = "지역 코드 조회 성공",
            content = @Content(schema = @Schema(implementation = ShopInfoResponse.class))
    )
    Response<List<CityCodeResponse>> getCityCodes();

}
