package backend.airo.api.clutrFatvl;

import backend.airo.api.clutrFatvl.dto.ClutrFatvInfoResponse;
import backend.airo.api.clutrFatvl.dto.ClutrFatvListResponse;
import backend.airo.api.global.swagger.ClutrFatvlControllerSwagger;
import backend.airo.api.global.dto.Response;
import backend.airo.application.clure_fatvl.usecase.ClutrFatvlUseCase;
import backend.airo.domain.clure_fatvl.ClutrFatvl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "ClutrFatvl", description = "전국 문화, 축제, 이벤트 관련 API V1")
public class ClutrFatvlController implements ClutrFatvlControllerSwagger {

    private final ClutrFatvlUseCase clutrFatvlUseCase;

    @Override
    @GetMapping("/clutr/fatvl")
    public Response<List<ClutrFatvListResponse>> getClureFatvlList(
            @RequestParam() @NotBlank(message = "시작 날짜는 필수 입니다.") String start,
            @RequestParam(defaultValue = "") String end
    ) {
        List<ClutrFatvl> clutrFatvlList = clutrFatvlUseCase.getClutrFatvlList();
        return Response.success(ClutrFatvListResponse.create(clutrFatvlList));
    }

    @Override
    @GetMapping("/clutr/fatvl/info")
    public Response<ClutrFatvInfoResponse> getClutrFatvlInfo(
            @RequestParam Long clutrFatvlId) {
        ClutrFatvl clutrFatvlInfo = clutrFatvlUseCase.getClutrFatvlInfo(clutrFatvlId);
        return Response.success(ClutrFatvInfoResponse.create(clutrFatvlInfo));
    }
}
