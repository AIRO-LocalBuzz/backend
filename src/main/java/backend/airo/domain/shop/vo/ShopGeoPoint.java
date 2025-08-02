package backend.airo.domain.shop.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record ShopGeoPoint(
        @Column(name = "latitude") Double lat,
        @Column(name = "longitude") Double lon
) {}
