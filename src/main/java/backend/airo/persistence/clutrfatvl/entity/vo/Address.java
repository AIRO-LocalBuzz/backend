package backend.airo.persistence.clutrfatvl.entity.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Address(
        @Column(name = "road_addr", length = 512) String road,
        @Column(name = "lot_addr",  length = 512) String lot
) {}