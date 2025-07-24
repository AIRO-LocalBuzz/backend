package backend.airo.domain.clure_fatvl.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.clure_fatvl.ClutrFatvl;

import java.util.List;

public interface ClutrFatvlRepository extends AggregateSupport<ClutrFatvl, Long> {

    List<ClutrFatvl> findAll();
}