package backend.airo.domain.rural_ex.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.rural_ex.RuralEx;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RuralExRepository extends AggregateSupport<RuralEx, Long> {

    Page<RuralEx> findAll(String megaName, String cityName, Pageable pageable);
}
