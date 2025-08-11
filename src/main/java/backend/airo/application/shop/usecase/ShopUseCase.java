package backend.airo.application.shop.usecase;

import backend.airo.cache.shop.ShopCacheService;
import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.query.GetShopListQuery;
import backend.airo.domain.shop.query.GetShopQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
@RequiredArgsConstructor
public class ShopUseCase {

    private final ShopCacheService shopCacheService;
    private final GetShopListQuery getShopListQuery;

    public Page<Shop> getShopList(String megaName, String cityName, Pageable pageable, String largeCategoryCode, String middleCategoryCode, String smallCategoryCode) {
        String middleCode = StringUtils.hasText(middleCategoryCode) ? middleCategoryCode : null;
        String smallCode = StringUtils.hasText(smallCategoryCode) ? smallCategoryCode : null;
        return getShopListQuery.handle(megaName, cityName, largeCategoryCode, middleCode, smallCode, pageable);
    }

    public Shop getShopInfo(Long shopId) {
        return shopCacheService.getShop(shopId);
    }


}
