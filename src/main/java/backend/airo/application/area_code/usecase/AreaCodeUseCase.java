package backend.airo.application.area_code.usecase;

import backend.airo.cache.area_code.AreaCodeCacheService;
import backend.airo.domain.area_code.CityCode;
import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.query.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AreaCodeUseCase {

    private final AreaCodeCacheService areaCodeCacheService;


    public List<MegaCode> getMegaCodeList() {
        return areaCodeCacheService.getMegaAllList();
    }

    public List<CityCode> getCityCodeList() {
        return areaCodeCacheService.getCityAllList();
    }

}
