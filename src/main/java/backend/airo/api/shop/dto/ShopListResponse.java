package backend.airo.api.shop.dto;

import backend.airo.domain.shop.Shop;
import lombok.Builder;

@Builder
public record ShopListResponse(

        Long id,
        String name,
        String lotAddr,
        String roadAddr,
        String indeScleName
) {

    public static ShopListResponse create(Shop shop) {
        return new ShopListResponse(
                shop.id(),
                shop.shopName(),
                shop.address().lot(),
                shop.address().road(),
                shop.shopType().getTypeName()
        );
    }

}
