package backend.airo.infra.open_api.rural_ex.client;

import backend.airo.infra.open_api.config.OpenApiFeignClientConfiguration;
import backend.airo.infra.open_api.rural_ex.dto.OpenApiRuralExResponse;
import backend.airo.infra.open_api.rural_ex.vo.RuralExInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 농림춘삭식품부, 해양수산부에서 제공하는 전국농어촌체험휴양마을 데이터 수집
 * <a href="https://www.data.go.kr/data/15013113/standard.do#tab_layer_open">...</a>
 */
@FeignClient(
        name = "openApi-rural-ex",
        url = "http://api.data.go.kr/openapi",
        configuration = OpenApiFeignClientConfiguration.class)
public interface OpenApiRuralExFeignClient {

    @GetMapping("/tn_pubr_public_frhl_exprn_vilage_api")
    OpenApiRuralExResponse<RuralExInfo> getClutrFatvlInfo(
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) String pageNo,
            @RequestParam(value = "numOfRows", defaultValue = "10000", required = false) String numOfRows
            );

}
