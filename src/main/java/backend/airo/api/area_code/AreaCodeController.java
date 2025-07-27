package backend.airo.api.area_code;

import backend.airo.api.area_code.dto.CityCodeResponse;
import backend.airo.api.area_code.dto.MegaCodeResponse;
import backend.airo.api.global.dto.Response;
import backend.airo.api.global.swagger.AreaCodeControllerSwagger;
import backend.airo.application.area_code.usecase.AreaCodeUseCase;
import backend.airo.domain.area_code.CityCode;
import backend.airo.domain.area_code.MegaCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "AreaCode", description = "전국 지역 코드 관련 API V1")
public class AreaCodeController implements AreaCodeControllerSwagger {

    private final AreaCodeUseCase areaCodeUseCase;

    @Override
    @GetMapping("/area/mega/code")
    public Response<List<MegaCodeResponse>> getMegaCodes(){
        List<MegaCode> megaCodes = areaCodeUseCase.getMegaCodeList();
        return Response.success(
                megaCodes.stream().map(list ->
                        new MegaCodeResponse(list.getId(), list.getCtprvnNm())
                ).toList()
        );
    }

    @Override
    @GetMapping("/area/city/code")
    public Response<List<CityCodeResponse>> getCityCodes() {
        List<CityCode> cityCodes = areaCodeUseCase.getCityCodeList();
        return Response.success(cityCodes.stream().map(list ->
                new CityCodeResponse(list.getId(), list.getCtprvnCd())
                ).toList());
    }
}
