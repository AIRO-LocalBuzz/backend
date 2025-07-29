package backend.airo.domain.area_code.query;

import backend.airo.domain.area_code.CityCode;
import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.repository.CityRepository;
import backend.airo.domain.area_code.repository.MegaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetCityAllCodeQuery {

    private final CityRepository cityRepository;

    public List<CityCode> handle() {
        return cityRepository.findAll();
    }

}
