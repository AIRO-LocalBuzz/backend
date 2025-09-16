package backend.airo.domain.shop.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record IndustryCodes(

        @Column(name = "inds_lcls_cd", length = 4,  nullable = false)
        String indsLclsCd,
        @Column(name = "inds_mcls_cd", length = 8,  nullable = false)
        String indsMclsCd,
        @Column(name = "inds_scls_cd", length = 14,  nullable = false)
        String indsSclsCd
) {}
