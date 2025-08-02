package backend.airo.domain.shop.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record IndustryCodes(
        @Column(name = "ksic_code",    length = 6,  nullable = false)
        String ksicCode,
        @Column(name = "inds_lcls_cd", length = 2,  nullable = false)
        String indsLclsCd,
        @Column(name = "inds_mcls_cd", length = 4,  nullable = false)
        String indsMclsCd,
        @Column(name = "inds_scls_cd", length = 7,  nullable = false)
        String indsSclsCd
) {}
