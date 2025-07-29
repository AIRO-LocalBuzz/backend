package backend.airo.api.global.swagger;

import backend.airo.api.clutr_fatvl.dto.ClutrFatvInfoResponse;
import backend.airo.api.clutr_fatvl.dto.ClutrFatvListResponse;
import backend.airo.api.global.dto.PageResponse;
import backend.airo.api.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ClutrFatvlControllerSwagger {



    @Operation(summary = "문화/축제 정보 상세 조회", description = "문화/축제 상세 정보 조회 API")
    @Parameters({
            @Parameter(name = "clutrFatvlId", description = "문화/축제 ID", example = "1")
    })
    @ApiResponse(
            responseCode = "200",
            description = "문화/축제 상세 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = ClutrFatvInfoResponse.class))
    )
    Response<ClutrFatvInfoResponse> getClutrFatvlInfo(@RequestParam Long clutrFatvlId);


    @Operation(summary = "전국 문화 축제 조회", description = "전국 문화 축제 조회 API")
    @Parameters({
            @Parameter(name = "megaCode", description = "도시 코드", example = "48"),
            @Parameter(name = "cityCode", description = "도시 지역구 코드", example = "48310"),
            @Parameter(name = "page", description = "시작 페이지 번호", example = "0"),
            @Parameter(name = "size", description = "페이지에 표시할 갯수", example = "20")
    })
    @ApiResponse(
            responseCode = "200",
            description = "문화 축제 정보 리스트 조회 성공",
            content = @Content(schema = @Schema(implementation = ClutrFatvListResponse.class))
    )
    @GetMapping("/clutr/fatvl")
    Response<PageResponse<ClutrFatvListResponse>> getClureFatvlList(
            @RequestParam() String megaCode,
            @RequestParam() String cityCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    );

}
