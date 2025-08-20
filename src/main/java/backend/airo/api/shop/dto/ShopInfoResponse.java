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
                .id(shop.id())
                .shopName(shop.shopName())
                .roadAddr(shop.address().road())
                .lotAddr(shop.address().lot())
                .flrNo(shop.floorInfo().flrNo())
                .hoNo(shop.floorInfo().hoNo())
                .build();
    }

}
