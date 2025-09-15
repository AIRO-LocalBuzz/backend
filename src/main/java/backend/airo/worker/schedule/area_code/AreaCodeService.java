package backend.airo.worker.schedule.area_code;

import backend.airo.domain.area_code.CityCode;
import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.command.CreateAllCityCodeCommand;
import backend.airo.domain.area_code.command.CreateAllMegaCodeCommand;
import backend.airo.domain.area_code.port.AreaCodePort;
import backend.airo.support.time.TimeCatch;
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

    private final AreaCodePort areaCodePort;
    private final CreateAllMegaCodeCommand createAllMegaCodeCommand;
    private final CreateAllCityCodeCommand createAllCityCodeCommand;
    private final TimeCatch timeCatch = new TimeCatch("AreaCode Time Check");

    public void collectCodeOf() {
        timeCatch.start();
        //1. 지역 코드 수집
        List<MegaCode> megaCodes = areaCodePort.getMegaCode();
        List<MegaCode> handle = createAllMegaCodeCommand.handle(megaCodes);

        // 2. 이름 기준으로 MegaCode ID 매핑
        Map<String, Long> megaCodeIdMap = handle.stream()
                .collect(Collectors.toMap(MegaCode::ctprvnNm, MegaCode::ctprvnCd));

        // 3. 시군구 코드 수집 및 MegaCode ID 할당
        List<CityCode> codeCodes = areaCodePort.getCityCode(megaCodes,megaCodeIdMap);

        createAllCityCodeCommand.handle(codeCodes);
//        List<AdmiCode> admiCode = asyncAreaCodeDataCollector.getAdmiCode(cityCode);
//        List<ZoneCode> zoneCode = asyncAreaCodeDataCollector.getZoneCode(cityCode);
        timeCatch.end();
    }
}
