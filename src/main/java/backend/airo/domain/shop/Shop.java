package backend.airo.domain.shop;

import backend.airo.domain.shop.vo.*;
import jakarta.persistence.Embedded;
import lombok.Getter;

/**
 * @param industry 업종 코드 묶음
 * @param region   행정 코드
 * @param address  주소 묶음
 * @param location 좌표 묶음
 */
public record Shop(
        Long contentId,
        Integer contentTypeId,
        String shopName,
        @Embedded
        IndustryCodes industry,
        @Embedded
        RegionCodes region,
        @Embedded
        ShopAddress address,
        @Embedded
        ShopGeoPoint location,
        String representativeImageURL,
        String thumbnailImageURl
) {

}
