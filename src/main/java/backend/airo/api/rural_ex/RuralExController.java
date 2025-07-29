//package backend.airo.api.rural_ex;
//
//import backend.airo.api.clutr_fatvl.dto.ClutrFatvListResponse;
//import backend.airo.api.global.dto.PageResponse;
//import backend.airo.api.global.dto.Response;
//import backend.airo.api.global.swagger.RuralExControllerSwagger;
//import backend.airo.application.rural_ex.usecase.RuralExUseCase;
//import backend.airo.domain.clure_fatvl.ClutrFatvl;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/v1")
//@RequiredArgsConstructor
//@Tag(name = "RuralEx", description = "전국 농어촌 체험 휴향 마을 관련 API V1")
//public class RuralExController implements RuralExControllerSwagger {
//
//    private RuralExUseCase ruralExUseCase;
//
//    @Override
//    @GetMapping("/clutr/fatvl")
//    public Response<PageResponse<ClutrFatvListResponse>> getClureFatvlList(
//            @RequestParam() String megaCode,
//            @RequestParam() String cityCode,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size
//    ) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<ClutrFatvl> clutrFatvls = ruralExUseCase.getClutrFatvlList(megaCode, cityCode, pageable);
//
//        List<ClutrFatvListResponse> content = clutrFatvls.getContent().stream().map(list ->
//                ClutrFatvListResponse.create(list, areaCodeCache.getMegaName(list.getAddress().megaCodeId()), areaCodeCache.getCityName(list.getAddress().ctprvnCodeId()))
//        ).toList();
//        return Response.success(
//                new PageResponse<>(
//                        content,
//                        clutrFatvls.getNumber(),
//                        clutrFatvls.getSize(),
//                        clutrFatvls.getTotalElements(),
//                        clutrFatvls.getTotalPages()
//                )
//        );
//    }
//
//}
