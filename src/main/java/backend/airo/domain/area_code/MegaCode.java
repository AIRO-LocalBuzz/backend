package backend.airo.domain.area_code;

import lombok.Getter;

@Getter
public class MegaCode {
    private Long id;
    private final String ctprvnCd;
    private final String ctprvnNm;

    public MegaCode(Long id, String ctprvnCd, String ctprvnNm) {
        this.id = id;
        this.ctprvnCd = ctprvnCd;
        this.ctprvnNm = ctprvnNm;
    }

    public MegaCode(String ctprvnCd, String ctprvnNm) {
        this.ctprvnCd = ctprvnCd;
        this.ctprvnNm = ctprvnNm;
    }
}
