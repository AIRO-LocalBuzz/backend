package backend.airo.domain.shop.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record ShopAddress(
        @Column(name = "road_addr", length = 512)
        String road,
        @Column(name = "lot_addr",  length = 512)
        String lot,
        String newZipcd
) {
    public String checkAddress() {
        if (road.isEmpty() && lot.isEmpty()) {
            return "주소가 없습니다. ";
        }
        if (road.isEmpty()) {
            return lot;
        }
        return road;
    }
}