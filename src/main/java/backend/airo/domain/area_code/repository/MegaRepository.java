package backend.airo.domain.area_code.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.area_code.CityCode;
import backend.airo.domain.area_code.MegaCode;

import java.util.List;

public interface MegaRepository extends AggregateSupport<MegaCode, Long> {

    List<MegaCode> findAll();

    MegaCode findByCtprvnCode(Long ctprvnCode);
}
