package backend.airo.domain.shop.vo;


import jakarta.persistence.Embeddable;

@Embeddable
public record RegionCodes(
        String ctprvnCd,
        String signguCd
) {}