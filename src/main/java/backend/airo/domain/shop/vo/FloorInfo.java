package backend.airo.domain.shop.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public record FloorInfo(
        String flrNo,
        String hoNo
) {
}
