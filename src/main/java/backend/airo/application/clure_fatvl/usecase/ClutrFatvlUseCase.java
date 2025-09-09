package backend.airo.application.clure_fatvl.usecase;

import backend.airo.cache.clutr_fatvl.ClutrFatvlCacheService;
import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.domain.clure_fatvl.query.GetClutrFatvlInfoQuery;
import backend.airo.domain.clure_fatvl.query.GetClutrFatvlListQuery;
import backend.airo.domain.clure_fatvl.query.GetClutrFatvlQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClutrFatvlUseCase {

    private final GetClutrFatvlListQuery getClutrFatvlListQuery;
    private final ClutrFatvlCacheService clutrFatvlCacheService;

    public ClutrFatvl getClutrFatvlInfo(Long clutrFatvlId) {
        return clutrFatvlCacheService.getClutrFatvl(clutrFatvlId);
    }

    public Page<ClutrFatvl> getClutrFatvlList(Integer megaName, Integer cityName, Pageable pageable) {
        return getClutrFatvlListQuery.handle(megaName, cityName, pageable);
    }


}
