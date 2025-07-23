package backend.airo.infra.open_api.client;

import backend.airo.infra.config.OpenApiFeignClientConfiguration;
import backend.airo.infra.open_api.dto.OpenApiResponse;
import backend.airo.infra.open_api.vo.ClutrFatvlInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "openApi",
        url = "http://api.data.go.kr/openapi",
        configuration = OpenApiFeignClientConfiguration.class)
public interface OpenApiFeignClient {

    @GetMapping("/tn_pubr_public_cltur_fstvl_api")
    OpenApiResponse<List<ClutrFatvlInfo>> getClutrFatvlInfo(
            @RequestParam(value = "fstvlStartDate") String fstvlStartDate,
            @RequestParam(value = "fstvlEndDate") String fstvlEndDate
            );

}
