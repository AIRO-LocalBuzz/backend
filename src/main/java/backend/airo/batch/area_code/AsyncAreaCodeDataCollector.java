package backend.airo.batch.area_code;

import backend.airo.infra.open_api.area_find.client.OpenApiAreaFeignClient;
import backend.airo.infra.open_api.area_find.vo.OpenApiCtyCode;
import backend.airo.infra.open_api.area_find.vo.OpenApiMegaCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AsyncAreaCodeDataCollector {

    private final OpenApiAreaFeignClient openApiAreaFeignClient;

    public List<OpenApiMegaCode> getMegaCode() {
        return openApiAreaFeignClient.getListSidos("mega").items();
    }

    public List<OpenApiCtyCode> getCityCode(List<OpenApiMegaCode> openApiMegaCode) {
        List<OpenApiCtyCode> cityList = new ArrayList<>();
        for (OpenApiMegaCode code : openApiMegaCode) {
            try {
                cityList.addAll(openApiAreaFeignClient.getListSigungusBySido("cty", code.ctprvnCd()).items());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
        return cityList;
    }
}
