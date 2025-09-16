package backend.airo.domain.clure_fatvl.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.clure_fatvl.ClutrFatvl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ClutrFatvlRepository extends AggregateSupport<ClutrFatvl, Long> {

    Page<ClutrFatvl> findAll(Integer megaName, Integer cityName, Pageable pageable);


}