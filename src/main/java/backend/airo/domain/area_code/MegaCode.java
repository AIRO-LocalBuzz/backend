package backend.airo.domain.area_code;

import lombok.Getter;

@Getter
public class MegaCode {
    private final Long ctprvnCd;
    private final String ctprvnNm;

    public MegaCode(Long ctprvnCd, String ctprvnNm) {
        this.ctprvnCd = ctprvnCd;
        this.ctprvnNm = ctprvnNm;
    }
}
