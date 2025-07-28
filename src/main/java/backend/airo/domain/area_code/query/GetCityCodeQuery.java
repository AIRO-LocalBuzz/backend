package backend.airo.domain.area_code.query;

import backend.airo.domain.area_code.CityCode;
import backend.airo.domain.area_code.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GetCityCodeQuery {

    private final CityRepository cityRepository;

    public CityCode handle(Long ctprvnCode) {
        return cityRepository.findByCtprvnCode(ctprvnCode);
    }

}
