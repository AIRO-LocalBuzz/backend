package backend.airo.api.global.swagger;

import backend.airo.api.clutr_fatvl.dto.ClutrFatvInfoResponse;
import backend.airo.api.clutr_fatvl.dto.ClutrFatvListResponse;
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
            @Parameter(name = "start", description = "시작 날짜", example = "2025-07-24"),
            @Parameter(name = "end  ", description = "종료 날짜"),
    })
    @ApiResponse(
            responseCode = "200",
            description = "문화 축제 정보 리스트 조회 성공",
            content = @Content(schema = @Schema(implementation = ClutrFatvListResponse.class))
    )
    @GetMapping("/clutr/fatvl")
    public Response<List<ClutrFatvListResponse>> getClureFatvlList(
            @RequestParam() @NotBlank(message = "시작 날짜는 필수 입니다.") String start,
            @RequestParam(defaultValue = "") String end
    );

}
