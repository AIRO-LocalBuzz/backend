package backend.airo.api.shop.dto;

import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.vo.ShopType;
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
                shop.getId(),
                shop.getShopName(),
                shop.getAddress().lot(),
                shop.getAddress().road(),
                shop.getShopType().getTypeName()
        );
    }

}
