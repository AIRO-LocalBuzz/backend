package backend.airo.api.shop.dto;

import backend.airo.domain.shop.Shop;
import backend.airo.api.shop.vo.ShopType;
import lombok.Builder;

@Builder
public record ShopInfoResponse(
        Long contentId,
        Integer contentTypeId,
        String name,
        String addr,
        String representativeImageURL,
        String thumbnailImageURl,
        String shopTypeName
) {

    public static ShopInfoResponse create(Shop shop) {
        return ShopInfoResponse.builder()
                .contentId(shop.contentId())
                .contentTypeId(shop.contentTypeId())
                .name(shop.shopName())
                .addr(shop.address().addr())
                .representativeImageURL(shop.representativeImageURL())
                .thumbnailImageURl(shop.thumbnailImageURl())
                .shopTypeName(ShopType.valueOf(shop.industry().indsSclsCd()).getName())
                .build();
    }

}
