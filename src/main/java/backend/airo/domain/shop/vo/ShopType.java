package backend.airo.domain.shop.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Embeddable
public record ShopType(
        @Column(name = "road_addr", length = 512)
        String road,
        @Column(name = "lot_addr",  length = 512)
        String lot
) {

}