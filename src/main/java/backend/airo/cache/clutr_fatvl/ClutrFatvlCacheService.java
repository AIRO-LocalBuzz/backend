package backend.airo.cache.clutr_fatvl;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.domain.clure_fatvl.query.GetClutrFatvlListQuery;
import backend.airo.domain.clure_fatvl.query.GetClutrFatvlQuery;
import backend.airo.support.cache.local.CacheName;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClutrFatvlCacheService {

    //TODO List는 페이지 기법이라 어떻게 할지 고민
    private final GetClutrFatvlListQuery getClutrFatvlListQuery;
    private final GetClutrFatvlQuery getClutrFatvlQuery;

    @Cacheable(cacheNames = {CacheName.CLUTR_FATVL_INFO_CACHE},  key = "#clureFatvl", sync = true)
    public ClutrFatvl getClutrFatvl(Long clureFatvl) {
        return getClutrFatvlQuery.handle(clureFatvl);
    }

    //문화 축제에 대한 캐시 정보 모두 삭제 ( 상세정보, List )
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheName.CLUTR_FATVL_INFO_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheName.CLUTR_FATVL_LIST_CACHE, allEntries = true)
    })
    public void evictClutrFatvl() {}
}
