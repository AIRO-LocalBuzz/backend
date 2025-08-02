package backend.airo.domain.area_code.port;

import backend.airo.domain.area_code.CityCode;
import backend.airo.domain.area_code.MegaCode;

import java.util.List;
import java.util.Map;

public interface AreaCodePort {
    List<MegaCode> getMegaCode();

    List<CityCode> getCityCode(List<MegaCode> megaCodes, Map<String, Long> megaCodeIdMap);
}
