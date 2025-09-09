package backend.airo.persistence.shop.entity;

import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.vo.*;
import backend.airo.persistence.abstracts.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shop",
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

    @Id
    private Long contentId;

    private Integer contentTypeId;

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

    private String representativeImageURL;

    private String thumbnailImageURl;


    public ShopEntity(Long contentId, Integer contentTypeId, String shopName, IndustryCodes industry, RegionCodes region, ShopAddress address, ShopGeoPoint location, String representativeImageURL, String thumbnailImageURl) {
        this.contentId = contentId;
        this.contentTypeId = contentTypeId;
        this.shopName = shopName;
        this.industry = industry;
        this.region = region;
        this.address = address;
        this.location = location;
        this.representativeImageURL = representativeImageURL;
        this.thumbnailImageURl = thumbnailImageURl;
    }

    public static ShopEntity toEntity(Shop shop) {
        return new ShopEntity(
                shop.contentId(),
                shop.contentTypeId(),
                shop.shopName(),
                shop.industry(),
                shop.region(),
                shop.address(),
                shop.location(),
                shop.representativeImageURL(),
                shop.thumbnailImageURl()
        );
    }

    public static Shop toDomain(ShopEntity shopEntity) {
        return new Shop(
                shopEntity.contentId,
                shopEntity.contentTypeId,
                shopEntity.shopName,
                shopEntity.industry,
                shopEntity.region,
                shopEntity.address,
                shopEntity.location,
                shopEntity.getRepresentativeImageURL(),
                shopEntity.getThumbnailImageURl()
        );
    }
}
