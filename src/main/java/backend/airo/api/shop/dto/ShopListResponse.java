package backend.airo.api.shop.dto;

import backend.airo.domain.shop.Shop;
import backend.airo.api.shop.vo.ShopType;
import lombok.Builder;

@Builder
public record ShopListResponse(

        Long contentId,
        Integer contentTypeId,
        String name,
        String addr,
        String representativeImageURL,
        String thumbnailImageURl,
        String shopTypeName
) {

    public static ShopListResponse create(Shop shop) {
        return new ShopListResponse(
                shop.contentId(),
                shop.contentTypeId(),
                shop.shopName(),
                shop.address().addr(),
                shop.representativeImageURL(),
                shop.thumbnailImageURl(),
                ShopType.valueOf(shop.industry().indsSclsCd()).getName()
        );
    }

}
