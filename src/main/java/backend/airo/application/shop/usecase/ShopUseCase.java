package backend.airo.application.shop.usecase;

import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.query.GetShopListQuery;
import backend.airo.domain.shop.query.GetShopQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ShopUseCase {

    private final GetShopListQuery getShopListQuery;
    private final GetShopQuery getShopQuery;

    public Page<Shop> getShopList(String megaName, String cityName, Pageable pageable) {
        return getShopListQuery.handle(megaName, cityName, pageable);
    }

    public Shop getShopInfo(Long shopId) {
        return getShopQuery.handle(shopId);
    }


}
