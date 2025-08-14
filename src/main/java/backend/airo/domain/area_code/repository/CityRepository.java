package backend.airo.domain.area_code.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.area_code.CityCode;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends AggregateSupport<CityCode, Long> {

    List<CityCode> findAll();

    Optional<Long> findByCityCode(Long megaId, String cityName);

    Optional<String> findByCityName(Long megaId, Long cityId);

}
