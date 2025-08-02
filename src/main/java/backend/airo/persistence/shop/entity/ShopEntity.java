package backend.airo.persistence.shop.entity;

import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.vo.*;
import backend.airo.persistence.abstracts.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shop_entity",
        indexes = {
                @Index(name = "idx_shop_region", columnList = "ctprvn_cd, signgu_cd"),
                @Index(
                        name = "idx_shop_region_category",
                        columnList = "ctprvn_cd, signgu_cd, inds_lcls_cd, inds_mcls_cd, inds_scls_cd"
                )
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ShopEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shopName", length = 200, nullable = false)
    private String shopName;

    //업종 코드 묶음
    @Embedded
    private IndustryCodes industry;

    //행정 코드
    @Embedded
    private RegionCodes region;

    //주소 묶음
    @Embedded
    private ShopAddress address;

    //좌표 묶음
    @Embedded
    private ShopGeoPoint location;

    //세부 주소[ 지점, 층, 호 ]
    @Embedded
    private FloorInfo floorInfo;

    @Enumerated(EnumType.STRING)
    private ShopType shopType;

    @Column(name = "brch_nm")
    private String brchNm;

    public ShopEntity(String shopName, IndustryCodes industry, RegionCodes region, ShopAddress address, ShopGeoPoint location, FloorInfo floorInfo, ShopType shopType, String brchNm) {
        this.shopName = shopName;
        this.industry = industry;
        this.region = region;
        this.address = address;
        this.location = location;
        this.floorInfo = floorInfo;
        this.shopType = shopType;
        this.brchNm = brchNm;
    }

    public static ShopEntity toEntity(Shop shop) {
        return new ShopEntity(
                shop.getShopName(),
                shop.getIndustry(),
                shop.getRegion(),
                shop.getAddress(),
                shop.getLocation(),
                shop.getFloorInfo(),
                shop.getShopType(),
                shop.getBrchNm()
        );
    }

    public static Shop toDomain(ShopEntity shopEntity) {
        return new Shop(
                shopEntity.id,
                shopEntity.shopName,
                shopEntity.industry,
                shopEntity.region,
                shopEntity.address,
                shopEntity.location,
                shopEntity.floorInfo,
                shopEntity.shopType,
                shopEntity.brchNm
        );
    }
}
