package backend.airo.persistence.area_code.entity;

import backend.airo.domain.area_code.MegaCode;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MegaCodeEntity {

    @Id
    private Long ctprvnCd;

    private String ctprvnNm;

    public MegaCodeEntity(Long ctprvnCd, String ctprvnNm) {
        this.ctprvnCd = ctprvnCd;
        this.ctprvnNm = ctprvnNm;
    }

    public static MegaCodeEntity toEntity(MegaCode megaCode) {
        return new MegaCodeEntity(megaCode.ctprvnCd(), megaCode.ctprvnNm());
    }

    public static MegaCode toDomain(MegaCodeEntity megaCodeEntity) {
        return new MegaCode(megaCodeEntity.ctprvnCd, megaCodeEntity.ctprvnNm);
    }

}
