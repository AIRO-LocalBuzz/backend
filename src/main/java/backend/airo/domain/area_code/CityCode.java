package backend.airo.domain.area_code;

import lombok.Getter;

@Getter
public class CityCode {

    private final Long ctprvnCd;

    private final String ctprvnNm;

    private final Long megaCodeId;

    public CityCode(Long ctprvnCd, String ctprvnNm, Long megaCodeId) {
        this.ctprvnCd = ctprvnCd;
        this.ctprvnNm = ctprvnNm;
        this.megaCodeId = megaCodeId;
    }
}
