package backend.airo.domain.area_code.query;

import backend.airo.domain.area_code.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GetCityCodeQuery {

    private final CityRepository cityRepository;

    public Long handle(Long megaCode, String cityName) {
        return cityRepository.findByCityCode(megaCode, cityName)
                //TODO 예외처리
                .orElseThrow(() -> new IllegalArgumentException("해당 도시를 찾을 수 없습니다."));
    }

}
