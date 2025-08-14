package backend.airo.domain.clure_fatvl.repository;

import backend.airo.domain.clure_fatvl.ClutrFatvl;

import java.util.List;

public interface ClutrFatvlBulkRepositoryPort {

    void batchInsert(List<ClutrFatvl> items);

}
