package backend.airo.api.clutrFatvl;

import backend.airo.api.clutrFatvl.dto.ClutrFatvInfoResponse;
import backend.airo.api.clutrFatvl.dto.ClutrFatvListResponse;
import backend.airo.api.global.dto.Response;
import backend.airo.application.clure_fatvl.usecase.ClutrFatvlUseCase;
import backend.airo.domain.clure_fatvl.ClutrFatvl;
import io.swagger.v3.oas.annotations.tags.Tag;
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
public class ClutrFatvlController {

    private final ClutrFatvlUseCase clutrFatvlUseCase;

    @GetMapping("/clutr/fatvl")
    public Response<List<ClutrFatvListResponse>> getClureFatvlList(@RequestParam String start,
                                                @RequestParam(defaultValue = "") String end) {
        List<ClutrFatvl> clutrFatvlList = clutrFatvlUseCase.getClutrFatvlList();
        return Response.success(ClutrFatvListResponse.create(clutrFatvlList));
    }

    @GetMapping("/clutr/fatvl/info")
    public Response<ClutrFatvInfoResponse> getClureFatvlList(@RequestParam Long clutrFatvlId) {
        ClutrFatvl clutrFatvlInfo = clutrFatvlUseCase.getClutrFatvlInfo(clutrFatvlId);
        return Response.success(ClutrFatvInfoResponse.create(clutrFatvlInfo));
    }
}
