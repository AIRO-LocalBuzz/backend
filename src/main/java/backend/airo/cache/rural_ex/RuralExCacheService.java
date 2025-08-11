package backend.airo.cache.rural_ex;

import backend.airo.domain.rural_ex.RuralEx;
import backend.airo.domain.rural_ex.query.GetRuralExQuery;
import backend.airo.domain.rural_ex.query.GetRuralListQuery;
import backend.airo.support.cache.local.CacheName;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RuralExCacheService {

    //TODO List는 페이지 기법이라 어떻게 할지 고민
    private final GetRuralListQuery getRuralListQuery;
    private final GetRuralExQuery getRuralExQuery;

    @Cacheable(cacheNames = {CacheName.RURAL_EX_INFO_CACHE},  key = "#ruralExId", sync = true)
    public RuralEx getRuralEx(Long ruralExId) {
        return getRuralExQuery.handle(ruralExId);
    }

    //농업 축제에 대한 캐시 정보 모두 삭제
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheName.RURAL_EX_LIST_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheName.RURAL_EX_INFO_CACHE, allEntries = true)
    })
    public void evictRuralEx() {}

}
