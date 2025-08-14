package backend.airo.cache.area_code;

import backend.airo.domain.area_code.CityCode;
import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.query.*;
import backend.airo.support.cache.local.CacheName;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaCodeCacheService {

    private final GetCityAllCodeQuery getCityAllCodeQuery;
    private final GetMegaAllCodeQuery getMegaAllCodeQuery;
    private final GetCityCodeQuery getCityCodeQuery;
    private final GetCityNameQuery getCityNameQuery;
    private final GetMegaCodeQuery getMegaCodeQuery;
    private final GetMegaNameQuery getMegaNameQuery;

    //지역, 지역구 코드 조회
    @Cacheable(cacheNames = {CacheName.MEGA_AREA_ALL_CACHE}, key = "'all'", sync = true)
    public List<MegaCode> getMegaAllList() {
        return getMegaAllCodeQuery.handle();
    }

    @Cacheable(cacheNames = {CacheName.CITY_AREA_ALL_CACHE}, key = "'all'", sync = true)
    public List<CityCode> getCityAllList() {
        return getCityAllCodeQuery.handle();
    }

    //백엔드 내부적으로 사용하기 위한 캐시
    @Cacheable(cacheNames = {CacheName.MEGA_AREA_ID_CACHE}, key = "#megaName", sync = true)
    public Long getMegaCode(String megaName) {
        return getMegaCodeQuery.handle(megaName);
    }

    @Cacheable(cacheNames = {CacheName.MEGA_AREA_NAME_CACHE}, key = "#megaId", sync = true)
    public String getMegaName(Long megaId) {
        return getMegaNameQuery.handle(megaId);
    }

    @Cacheable(cacheNames = {CacheName.CITY_AREA_ID_CACHE}, key = "#megaId + ':' + T(org.springframework.util.StringUtils).trimAllWhitespace(#cityName).toLowerCase()", sync = true)
    public Long getCityCode(Long megaId,String cityName) {
        return getCityCodeQuery.handle(megaId,cityName);
    }

    @Cacheable(cacheNames = {CacheName.CITY_AREA_NAME_CACHE}, key = "#megaId + ':' + #cityId", sync = true)
    public String getCityName(Long megaId, Long cityId) {
        return getCityNameQuery.handle(megaId,cityId);
    }

    //지역 코드에 대한 모든 정보 삭제
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheName.MEGA_AREA_ALL_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheName.MEGA_AREA_ID_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheName.MEGA_AREA_NAME_CACHE, allEntries = true)
    })
    public void evictMegaCode() {}

    @Caching(evict = {
            @CacheEvict(cacheNames = CacheName.CITY_AREA_ALL_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheName.CITY_AREA_ID_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheName.CITY_AREA_NAME_CACHE, allEntries = true)
    })
    public void evictCityCode() {}

    //TODO 추후 캐시 무효화 전략도 필요함 ( 6개월 마다 지역 코드, 지역구 코드를 조회 하기에 )
}
