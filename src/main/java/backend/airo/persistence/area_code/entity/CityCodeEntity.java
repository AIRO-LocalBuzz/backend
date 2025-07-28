package backend.airo.persistence.area_code.entity;

import backend.airo.domain.area_code.CityCode;
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
public class CityCodeEntity {

    @Id
    private Long ctprvnCd;

    private String ctprvnNm;

    private Long megaCodeId;

    public CityCodeEntity(Long ctprvnCd, String ctprvnNm, Long megaCodeId) {
        this.ctprvnCd = ctprvnCd;
        this.ctprvnNm = ctprvnNm;
        this.megaCodeId = megaCodeId;
    }

    public static CityCodeEntity toEntity(CityCode cityCode) {
        return new CityCodeEntity(cityCode.getCtprvnCd(), cityCode.getCtprvnNm(), cityCode.getMegaCodeId());
    }

    public static CityCode toDomain(CityCodeEntity cityCodeEntity) {
        return new CityCode(cityCodeEntity.ctprvnCd, cityCodeEntity.ctprvnNm, cityCodeEntity.megaCodeId);
    }
}
