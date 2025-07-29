package backend.airo.application.area_code.usecase;

import backend.airo.domain.area_code.CityCode;
import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.query.GetCityAllCodeQuery;
import backend.airo.domain.area_code.query.GetMegaAllCodeQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AreaCodeUseCase {

    private final GetMegaAllCodeQuery getMegaAllCodeQuery;
    private final GetCityAllCodeQuery getCityAllCodeQuery;


    public List<MegaCode> getMegaCodeList() {
        return getMegaAllCodeQuery.handle();
    }

    public List<CityCode> getCityCodeList() {
        return getCityAllCodeQuery.handle();
    }

}
