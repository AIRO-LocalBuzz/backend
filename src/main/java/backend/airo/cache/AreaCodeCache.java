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

    private Map<String, String> megaMap; // key: 시도명 → 코드
    private Map<String, String> cityMap; // key: (시도코드::시군구명) → 시군구코드

    @PostConstruct
    public void init() {
        List<MegaCode> megaCodes = getMegaAllCodeQuery.handle();
        List<CityCode> cityCodes = getCityAllCodeQuery.handle();

        megaMap = megaCodes.stream()
                .collect(Collectors.toMap(MegaCode::getCtprvnNm, m -> String.valueOf(m.getCtprvnCd())));

        cityMap = cityCodes.stream()
                .collect(Collectors.toMap(
                        c -> getCityKey(String.valueOf(c.getMegaCodeId()), c.getCtprvnNm()),
                        c -> String.valueOf(c.getCtprvnCd())
                ));
        log.info("AreaCodeMapper 초기화 성공 : megaCode = ${}, cityCode = ${}", megaCodes.size(), cityCodes.size() );
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
}
