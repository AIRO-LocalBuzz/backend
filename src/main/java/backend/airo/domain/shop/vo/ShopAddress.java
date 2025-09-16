package backend.airo.domain.shop.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record ShopAddress(
        @Column(name = "road_addr", length = 512)
        String addr,
        @Column(name = "lot_addr",  length = 512)
        String addrInfo
) {
}