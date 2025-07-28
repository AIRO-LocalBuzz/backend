package backend.airo.domain.clure_fatvl.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record GeoPoint(
        @Column(name = "latitude") Double lat,
        @Column(name = "longitude") Double lon
) {}
