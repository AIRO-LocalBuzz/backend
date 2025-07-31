package backend.airo.api.rural_ex;

import backend.airo.api.global.dto.PageResponse;
import backend.airo.api.global.dto.Response;
import backend.airo.api.global.swagger.RuralExControllerSwagger;
import backend.airo.api.rural_ex.dto.RuralExInfoResponse;
import backend.airo.api.rural_ex.dto.RuralExListResponse;
import backend.airo.application.rural_ex.usecase.RuralExUseCase;
import backend.airo.cache.AreaCodeCache;
import backend.airo.domain.rural_ex.RuralEx;
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
@Tag(name = "RuralEx", description = "전국 농어촌 체험 휴향 마을 관련 API V1")
public class RuralExController implements RuralExControllerSwagger {

    private final RuralExUseCase ruralExUseCase;
    //TODO Redis로 바꿀 것
    private final AreaCodeCache areaCodeCache;

    @Override
    @GetMapping("/rural/ex")
    public Response<PageResponse<RuralExListResponse>> getClureFatvlList(
            @RequestParam() String megaCode,
            @RequestParam() String cityCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RuralEx> ruralExes = ruralExUseCase.getRuralListQuery(areaCodeCache.getMegaName(megaCode), areaCodeCache.getCityName(cityCode), pageable);
        List<RuralExListResponse> content = ruralExes.getContent().stream().map(RuralExListResponse::create).toList();
        return Response.success(
                new PageResponse<>(
                        content,
                        ruralExes.getNumber(),
                        ruralExes.getSize(),
                        ruralExes.getTotalElements(),
                        ruralExes.getTotalPages()
                )
        );
    }

    @Override
    @GetMapping("/rural/ex/info")
    public Response<RuralExInfoResponse> getClutrFatvlInfo(
            @RequestParam Long ruralExId) {
        RuralEx ruralEx = ruralExUseCase.getClutrFatvlInfo(ruralExId);
        return Response.success(RuralExInfoResponse.create(ruralEx));
    }

}
