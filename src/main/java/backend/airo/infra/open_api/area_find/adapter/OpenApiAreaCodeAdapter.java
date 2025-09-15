package backend.airo.infra.open_api.area_find.adapter;

import backend.airo.domain.area_code.CityCode;
import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.port.AreaCodePort;
import backend.airo.infra.open_api.area_find.client.OpenApiAreaFeignClient;
import backend.airo.infra.open_api.area_find.vo.OpenApiCtyCode;
import backend.airo.infra.open_api.area_find.vo.OpenApiMegaCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenApiAreaCodeAdapter implements AreaCodePort {

    private final OpenApiAreaFeignClient openApiAreaFeignClient;

    @Operation
    public List<CityCode> getCityCode(List<MegaCode> megaCodes, Map<String, Long> megaCodeIdMap) {
        List<OpenApiCtyCode> cityList = new ArrayList<>();
        for (MegaCode code : megaCodes) {
            try {
                cityList.addAll(openApiAreaFeignClient.getListSigungusBySido("cty", code.ctprvnCd().toString()).items());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }

        return cityList.stream()
                .map(list -> {
                    Long megaCodeId = megaCodeIdMap.get(list.ctprvnNm());
                    return new CityCode(Long.valueOf(list.signguCd()),list.signguNm(), megaCodeId);
                })
                .toList();

    }

    @Override
    public List<MegaCode> getMegaCode() {
        List<OpenApiMegaCode> megaCodeInfos = openApiAreaFeignClient.getListSidos("mega").items();
        return megaCodeInfos.stream()
                .map(list -> new MegaCode(Long.valueOf(list.ctprvnCd()), list.ctprvnNm()))
                .toList();
    }
}
