package backend.airo.persistence.area_code.entity;

import backend.airo.domain.area_code.MegaCode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MegaCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ctprvnCd;

    private String ctprvnNm;

    public MegaCodeEntity(String ctprvnCd, String ctprvnNm) {
        this.ctprvnCd = ctprvnCd;
        this.ctprvnNm = ctprvnNm;
    }

    public static MegaCodeEntity toEntity(MegaCode megaCode) {
        return new MegaCodeEntity(megaCode.getCtprvnCd(), megaCode.getCtprvnNm());
    }

    public static MegaCode toDomain(MegaCodeEntity megaCodeEntity) {
        return new MegaCode(megaCodeEntity.id, megaCodeEntity.ctprvnCd, megaCodeEntity.ctprvnNm);
    }

}
