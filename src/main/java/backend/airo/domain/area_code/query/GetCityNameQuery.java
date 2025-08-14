package backend.airo.domain.area_code.query;

import backend.airo.domain.area_code.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetCityNameQuery {

    private final CityRepository cityRepository;

    public String handle(Long megaId, Long cityId) {
        return cityRepository.findByCityName(megaId, cityId)
                .orElseThrow(() -> new IllegalArgumentException("해당 도시를 찾을 수 없습니다."));
    }
}
