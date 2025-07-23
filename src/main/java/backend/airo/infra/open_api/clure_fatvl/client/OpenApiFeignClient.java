package backend.airo.infra.open_api.clure_fatvl.client;

import backend.airo.infra.open_api.clure_fatvl.dto.OpenApiClureFatvlResponse;
import backend.airo.infra.open_api.clure_fatvl.vo.ClutrFatvlInfo;
import backend.airo.infra.open_api.config.OpenApiFeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 문화체육관광부에서 제공하는 전국문화축제표준 데이터(Open API)를 이용한 행사, 축제 등 데이터 수집
 * <a href="https://www.data.go.kr/data/15013104/standard.do">...</a>
 */
@FeignClient(
        name = "openApi",
        url = "http://api.data.go.kr/openapi",
        configuration = OpenApiFeignClientConfiguration.class)
public interface OpenApiFeignClient {

    @GetMapping("/tn_pubr_public_cltur_fstvl_api")
    OpenApiClureFatvlResponse<List<ClutrFatvlInfo>> getClutrFatvlInfo(
            @RequestParam(value = "fstvlStartDate") String fstvlStartDate,
            @RequestParam(value = "fstvlEndDate") String fstvlEndDate
            );

}
