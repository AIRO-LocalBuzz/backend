package backend.airo.domain.clure_fatvl.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Address(
        @Column(name = "road_addr", length = 512)
        String road,
        @Column(name = "lot_addr",  length = 512)
        String lot,
        String megaCodeId,
        String ctprvnCodeId
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