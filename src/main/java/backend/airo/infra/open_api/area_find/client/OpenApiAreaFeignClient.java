package backend.airo.infra.open_api.area_find.client;

import backend.airo.infra.open_api.area_find.config.OpenApiFeignAreaClientConfiguration;
import backend.airo.infra.open_api.area_find.dto.OpenApiAreaCodeResponse;
import backend.airo.infra.open_api.area_find.vo.OpenApiAdmiCode;
import backend.airo.infra.open_api.area_find.vo.OpenApiCtyCode;
import backend.airo.infra.open_api.area_find.vo.OpenApiMegaCode;
import backend.airo.infra.open_api.area_find.vo.OpenApiZoneCode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 각 도시의 행정구역, 시군구, 행정동, 법정동을 조회한다.
 * <a href="https://www.data.go.kr/data/15012005/openapi.do">...</a>
 */
@FeignClient(
        name = "openApi-area-find",
        url = "https://apis.data.go.kr",
        configuration = OpenApiFeignAreaClientConfiguration.class)
public interface OpenApiAreaFeignClient {

    //행정 구역 조회 ( 서울시, 대전 등등 )
    @GetMapping("B553077/api/open/sdsc2/baroApi")
    OpenApiAreaCodeResponse<OpenApiMegaCode> getListSidos(
            @RequestParam(value = "catId", defaultValue = "mega", required = false) String catId
            );

    //시군구 조회 [ param -> 시도 코드 ]
    @GetMapping("B553077/api/open/sdsc2/baroApi")
    OpenApiAreaCodeResponse<OpenApiCtyCode> getListSigungusBySido(
            @RequestParam(value = "catId", defaultValue = "cty", required = false) String catId,
            @RequestParam(value = "ctprvnCd", required = false) String ctprvnCd
    );

    //행정동 조회 [ param -> 시군구 코드 ]
    @GetMapping("B553077/api/open/sdsc2/baroApi")
    OpenApiAreaCodeResponse<OpenApiAdmiCode> getListAdmDongsBySigungu(
            @RequestParam(value = "catId", defaultValue = "admi", required = false) String catId,
            @RequestParam(value = "signguCd", required = false) String signguCd
    );

    //법정동 조회 [ param -> 시군구 코드 ]
    @GetMapping("B553077/api/open/sdsc2/baroApi")
    OpenApiAreaCodeResponse<OpenApiZoneCode> getListLegalDongsBySigungu(
            @RequestParam(value = "catId", defaultValue = "zone", required = false) String catId,
            @RequestParam(value = "signguCd", required = false) String signguCd
    );

}
