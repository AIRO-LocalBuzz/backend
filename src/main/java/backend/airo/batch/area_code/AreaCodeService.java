package backend.airo.batch.area_code;

import backend.airo.domain.area_code.CityCode;
import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.command.CreateAllCityCodeCommand;
import backend.airo.domain.area_code.command.CreateAllMegaCodeCommand;
import backend.airo.infra.open_api.area_find.vo.OpenApiCtyCode;
import backend.airo.infra.open_api.area_find.vo.OpenApiMegaCode;
import backend.airo.support.TimeCatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AreaCodeService {

    private final AsyncAreaCodeDataCollector asyncAreaCodeDataCollector;
    private final CreateAllMegaCodeCommand createAllMegaCodeCommand;
    private final CreateAllCityCodeCommand createAllCityCodeCommand;
    private final TimeCatch timeCatch = new TimeCatch("AreaCode Time Check");

//    private final

    public void collectCodeOf() {
        timeCatch.start();
        List<OpenApiMegaCode> openApiMegaCode = asyncAreaCodeDataCollector.getMegaCode();
        List<MegaCode> megaCodes = openApiMegaCode.stream()
                .map(list -> new MegaCode(list.ctprvnCd(), list.ctprvnNm()))
                .toList();
        List<MegaCode> handle = createAllMegaCodeCommand.handle(megaCodes);

        // 2. 이름 기준으로 MegaCode ID 매핑
        Map<String, Long> megaCodeIdMap = handle.stream()
                .collect(Collectors.toMap(MegaCode::getCtprvnNm, MegaCode::getId));

        // 3. 시군구 코드 수집 및 MegaCode ID 할당
        List<OpenApiCtyCode> openApiCtyCodes = asyncAreaCodeDataCollector.getCityCode(openApiMegaCode);
        List<CityCode> codeCodes = openApiCtyCodes.stream()
                .map(list -> {
                    Long megaCodeId = megaCodeIdMap.get(list.ctprvnNm());
                    return new CityCode(list.signguCd(),list.signguNm(), megaCodeId);
                })
                .toList();
        createAllCityCodeCommand.handle(codeCodes);
//        List<AdmiCode> admiCode = asyncAreaCodeDataCollector.getAdmiCode(cityCode);
//        List<ZoneCode> zoneCode = asyncAreaCodeDataCollector.getZoneCode(cityCode);
        timeCatch.end();
    }
}
