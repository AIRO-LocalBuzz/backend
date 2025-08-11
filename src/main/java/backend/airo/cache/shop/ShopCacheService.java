package backend.airo.cache.shop;

import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.query.GetShopListQuery;
import backend.airo.domain.shop.query.GetShopQuery;
import backend.airo.support.cache.local.CacheName;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopCacheService {

    //TODO List는 페이지 기법이라 어떻게 할지 고민
    private final GetShopListQuery getShopListQuery;
    private final GetShopQuery getShopQuery;

    @Cacheable(cacheNames = {CacheName.SHOP_INFO_CACHE},  key = "#shopId", sync = true)
    public Shop getShop(Long shopId) {
        return getShopQuery.handle(shopId);
    }

    //소상 공인에 대한 캐시 정보 모두 삭제
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheName.SHOP_LIST_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheName.SHOP_INFO_CACHE, allEntries = true)
    })
    public void evictShop() {}

}
