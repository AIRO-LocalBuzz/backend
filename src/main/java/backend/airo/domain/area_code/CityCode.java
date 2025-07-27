package backend.airo.domain.area_code;

import lombok.Getter;

@Getter
public class CityCode {

    private Long id;

    private final String ctprvnCd;

    private final String ctprvnNm;

    private final String megaCodeId;

    public CityCode(Long id, String ctprvnCd, String ctprvnNm, String megaCodeId) {
        this.id = id;
        this.ctprvnCd = ctprvnCd;
        this.ctprvnNm = ctprvnNm;
        this.megaCodeId = megaCodeId;
    }

    public CityCode(String ctprvnCd, String ctprvnNm, String megaCodeId) {
        this.ctprvnCd = ctprvnCd;
        this.ctprvnNm = ctprvnNm;
        this.megaCodeId = megaCodeId;
    }
}
