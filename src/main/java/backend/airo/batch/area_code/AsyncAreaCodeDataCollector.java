package backend.airo.batch.area_code;

import backend.airo.infra.open_api.area_find.client.OpenApiAreaFeignClient;
import backend.airo.infra.open_api.area_find.vo.OpenApiAdmiCode;
import backend.airo.infra.open_api.area_find.vo.OpenApiCtyCode;
import backend.airo.infra.open_api.area_find.vo.OpenApiMegaCode;
import backend.airo.infra.open_api.area_find.vo.OpenApiZoneCode;
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

    public List<OpenApiAdmiCode> getAdmiCode(List<OpenApiCtyCode> signguCd) {
        List<OpenApiAdmiCode> admiList = new ArrayList<>();
        for (OpenApiCtyCode openApiCtyCode : signguCd) {
            try {
                admiList.addAll(openApiAreaFeignClient.getListAdmDongsBySigungu("admi", openApiCtyCode.signguCd()).items());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
        return admiList;
    }

    public List<OpenApiZoneCode> getZoneCode(List<OpenApiCtyCode> signguCd) {
        List<OpenApiZoneCode> zoneList = new ArrayList<>();
        for (OpenApiCtyCode openApiCtyCode : signguCd) {
            try {
                zoneList.addAll(openApiAreaFeignClient.getListLegalDongsBySigungu("zone", openApiCtyCode.signguCd()).items());
                Thread.sleep(150L);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
        return zoneList;
    }
}
