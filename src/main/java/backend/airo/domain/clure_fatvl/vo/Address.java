package backend.airo.domain.clure_fatvl.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Address(
        @Column(name = "addr1", length = 512)
        String addr1,
        @Column(name = "addr2",  length = 512)
        String addr2,
        Integer megaCodeId,
        Integer ctprvnCodeId
) {

}