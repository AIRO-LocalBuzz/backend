package backend.airo.domain.area_code.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.area_code.CityCode;

import java.util.List;

public interface CityRepository extends AggregateSupport<CityCode, Long> {

    List<CityCode> findAll();

    CityCode findByCtprvnCode(Long ctprvnCode);
}
