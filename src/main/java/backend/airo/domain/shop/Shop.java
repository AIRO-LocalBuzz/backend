package backend.airo.domain.shop;

import backend.airo.domain.shop.vo.*;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

public record Shop(
        Long id,
        String shopName,
        IndustryCodes industry,
        RegionCodes region,
        ShopAddress address,
        ShopGeoPoint location,
        FloorInfo floorInfo,
        ShopType shopType,
        String brchNm
) {
    public Shop(String shopName, IndustryCodes industry, RegionCodes region,
                ShopAddress address, ShopGeoPoint location, FloorInfo floorInfo,
                ShopType shopType, String brchNm) {
        this(null, shopName, industry, region, address, location, floorInfo, shopType, brchNm);
    }
}
