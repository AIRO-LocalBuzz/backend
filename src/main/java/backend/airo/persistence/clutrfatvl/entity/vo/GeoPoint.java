package backend.airo.persistence.clutrfatvl.entity.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record GeoPoint(
        @Column(name = "latitude",  precision = 10, scale = 7) Double lat,
        @Column(name = "longitude", precision = 10, scale = 7) Double lon
) {}
