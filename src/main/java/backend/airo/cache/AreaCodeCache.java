package backend.airo.cache;

import backend.airo.domain.area_code.CityCode;
import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.query.GetCityAllCodeQuery;
import backend.airo.domain.area_code.query.GetMegaAllCodeQuery;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AreaCodeCache {
    private final GetMegaAllCodeQuery getMegaAllCodeQuery;
    private final GetCityAllCodeQuery getCityAllCodeQuery;

    private Map<String, String> megaMap;
    private Map<String, String> megaCodeToNameMap;

    private Map<String, String> cityMap;
    private Map<String, String> cityCodeToNameMap;

    @PostConstruct
    public void init() {
        List<MegaCode> megaCodes = getMegaAllCodeQuery.handle();
        List<CityCode> cityCodes = getCityAllCodeQuery.handle();

        megaMap = megaCodes.stream()
                .collect(Collectors.toMap(MegaCode::getCtprvnNm, m -> String.valueOf(m.getCtprvnCd())));

        megaCodeToNameMap = megaCodes.stream()
                .collect(Collectors.toMap(m -> String.valueOf(m.getCtprvnCd()), MegaCode::getCtprvnNm));

        cityMap = cityCodes.stream()
                .collect(Collectors.toMap(
                        c -> getCityKey(String.valueOf(c.getMegaCodeId()), c.getCtprvnNm()),
                        c -> String.valueOf(c.getCtprvnCd())
                ));

        cityCodeToNameMap = cityCodes.stream()
                .collect(Collectors.toMap(
                        c -> String.valueOf(c.getCtprvnCd()),
                        CityCode::getCtprvnNm
                ));

        log.info("AreaCodeMapper 초기화 성공 : megaCode = {}, cityCode = {}", megaCodes.size(), cityCodes.size());
    }

    private String getCityKey(String megaCode, String cityName) {
        return megaCode + "::" + cityName;
    }

    public String getMegaCode(String megaName) {
        return megaMap.getOrDefault(megaName, "UNKNOWN");
    }

    public String getCityCode(String megaName, String cityName) {
        String megaCode = getMegaCode(megaName);
        return cityMap.getOrDefault(getCityKey(megaCode, cityName), "UNKNOWN");
    }

    public String getMegaName(String megaCode) {
        return megaCodeToNameMap.getOrDefault(megaCode, "UNKNOWN");
    }

    public String getCityName(String cityCode) {
        return cityCodeToNameMap.getOrDefault(cityCode, "UNKNOWN");
    }
}
