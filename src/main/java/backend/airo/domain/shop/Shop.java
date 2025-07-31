package backend.airo.domain.shop;

import backend.airo.domain.shop.vo.*;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public class Shop {


    private Long id;

    private final String shopName;

    //업종 코드 묶음
    @Embedded
    private final IndustryCodes industry;

    //행정 코드
    @Embedded
    private final RegionCodes region;

    //주소 묶음
    @Embedded
    private final ShopAddress address;

    //좌표 묶음
    @Embedded
    private final ShopGeoPoint location;

    //세부 주소[ 지점, 층, 호 ]
    @Embedded
    private final FloorInfo floorInfo;

    @Enumerated(EnumType.STRING)
    private final ShopType shopType;

    private final String brchNm;

    public Shop(Long id, String shopName, IndustryCodes industry, RegionCodes region, ShopAddress address, ShopGeoPoint location, FloorInfo floorInfo, ShopType shopType, String brchNm) {
        this.id = id;
        this.shopName = shopName;
        this.industry = industry;
        this.region = region;
        this.address = address;
        this.location = location;
        this.floorInfo = floorInfo;
        this.shopType = shopType;
        this.brchNm = brchNm;
    }

    public Shop(String shopName, IndustryCodes industry, RegionCodes region, ShopAddress address, ShopGeoPoint location, FloorInfo floorInfo, ShopType shopType , String brchNm) {
        this.shopName = shopName;
        this.industry = industry;
        this.region = region;
        this.address = address;
        this.location = location;
        this.floorInfo = floorInfo;
        this.shopType = shopType;
        this.brchNm = brchNm;
    }
}
