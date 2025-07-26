package backend.airo.domain.shop.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public record IndustryCodes(
        String ksicCode,
        String indsLclsCd,
        String indsMclsCd,
        String indsSclsCd
) {}
