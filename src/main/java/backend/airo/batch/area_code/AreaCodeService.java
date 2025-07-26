package backend.airo.batch.area_code;

import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.command.CreateAllMegaCodeCommand;
import backend.airo.infra.open_api.area_find.vo.OpenApiCtyCode;
import backend.airo.infra.open_api.area_find.vo.OpenApiMegaCode;
import backend.airo.support.TimeCatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AreaCodeService {

    private final AsyncAreaCodeDataCollector asyncAreaCodeDataCollector;
    private final CreateAllMegaCodeCommand createAllMegaCodeCommand;
    private final TimeCatch timeCatch = new TimeCatch("AreaCode Time Check");

//    private final

    public void collectCodeOf() {
        timeCatch.start();
        List<OpenApiMegaCode> openApiMegaCode = asyncAreaCodeDataCollector.getMegaCode();
        List<MegaCode> megaCodes = openApiMegaCode.stream().map(list -> new MegaCode(list.ctprvnCd(), list.ctprvnNm())).toList();
        createAllMegaCodeCommand.handle(megaCodes);
        List<OpenApiCtyCode> cityCode = asyncAreaCodeDataCollector.getCityCode(openApiMegaCode);
//        List<AdmiCode> admiCode = asyncAreaCodeDataCollector.getAdmiCode(cityCode);
//        List<ZoneCode> zoneCode = asyncAreaCodeDataCollector.getZoneCode(cityCode);
        timeCatch.end();
    }
}
