package backend.airo.api.clutr_fatvl;

import backend.airo.api.clutr_fatvl.dto.ClutrFatvInfoResponse;
import backend.airo.api.clutr_fatvl.dto.ClutrFatvListResponse;
import backend.airo.api.global.dto.PageResponse;
import backend.airo.api.global.dto.Response;
import backend.airo.api.global.swagger.ClutrFatvlControllerSwagger;
import backend.airo.application.clure_fatvl.usecase.ClutrFatvlUseCase;
import backend.airo.cache.AreaCodeCache;
import backend.airo.domain.clure_fatvl.ClutrFatvl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    //TODO 임시 캐시 -> Redis로 변경할 에정 [ 2차 개발 때 ]
    private final AreaCodeCache areaCodeCache;

    @Override
    @GetMapping("/clutr/fatvl")
    public Response<PageResponse<ClutrFatvListResponse>> getClureFatvlList(
            @RequestParam() String megaCode,
            @RequestParam() String cityCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClutrFatvl> clutrFatvls = clutrFatvlUseCase.getClutrFatvlList(megaCode, cityCode, pageable);

        List<ClutrFatvListResponse> content = clutrFatvls.getContent().stream().map(list ->
                ClutrFatvListResponse.create(list, areaCodeCache.getMegaName(list.getAddress().megaCodeId()), areaCodeCache.getCityName(list.getAddress().ctprvnCodeId()))
        ).toList();
        return Response.success(
                new PageResponse<>(
                        content,
                        clutrFatvls.getNumber(),
                        clutrFatvls.getSize(),
                        clutrFatvls.getTotalElements(),
                        clutrFatvls.getTotalPages()
                )
        );
    }

    @Override
    @GetMapping("/clutr/fatvl/info")
    public Response<ClutrFatvInfoResponse> getClutrFatvlInfo(
            @RequestParam Long clutrFatvlId) {
        ClutrFatvl clutrFatvlInfo = clutrFatvlUseCase.getClutrFatvlInfo(clutrFatvlId);
        return Response.success(ClutrFatvInfoResponse.create(clutrFatvlInfo));
    }
}
