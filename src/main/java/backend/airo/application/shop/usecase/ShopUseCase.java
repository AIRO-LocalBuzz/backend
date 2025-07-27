package backend.airo.application.shop.usecase;

import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.query.GetShopListQuery;
import backend.airo.domain.shop.query.GetShopQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShopUseCase {

    private final GetShopListQuery getShopListQuery;
    private final GetShopQuery getShopQuery;

    public List<Shop> getShopList(String megaName, String cityName) {
        return getShopListQuery.handle(megaName, cityName);
    }

    public Shop getShopInfo(Long shopId) {
        return getShopQuery.handle(shopId);
    }


}
