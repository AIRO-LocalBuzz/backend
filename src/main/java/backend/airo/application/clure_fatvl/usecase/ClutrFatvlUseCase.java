package backend.airo.application.clure_fatvl.usecase;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.domain.clure_fatvl.query.GetClutrFatvlListQuery;
import backend.airo.domain.clure_fatvl.query.GetClutrFatvlQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClutrFatvlUseCase {

    private final GetClutrFatvlQuery getClutrFatvlQuery;
    private final GetClutrFatvlListQuery getClutrFatvlListQuery;

    public ClutrFatvl getClutrFatvlInfo(Long clutrFatvlId) {
        return getClutrFatvlQuery.handle(clutrFatvlId);
    }

    public Page<ClutrFatvl> getClutrFatvlList(String megaName, String cityName, Pageable pageable) {
        return getClutrFatvlListQuery.handle(megaName, cityName, pageable);
    }


}
