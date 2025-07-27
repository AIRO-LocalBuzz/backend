package backend.airo.api.shop.dto;

import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.vo.ShopType;
import lombok.Builder;

@Builder
public record ShopInfoResponse(
        Long id,
        String shopName,
        String roadAddr,
        String lotAddr,
        String flrNo,
        String hoNo,
        ShopType shopType
) {

    public static ShopInfoResponse create(Shop shop) {
        return ShopInfoResponse.builder()
                .id(shop.getId())
                .shopName(shop.getShopName())
                .roadAddr(shop.getAddress().road())
                .lotAddr(shop.getAddress().lot())
                .flrNo(shop.getFloorInfo().flrNo())
                .hoNo(shop.getFloorInfo().hoNo())
                .build();
    }

}
