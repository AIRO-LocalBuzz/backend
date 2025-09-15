package backend.airo.domain.area_code;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CityCode(
        Long ctprvnCd,
        String ctprvnNm,
        Long megaCodeId) {

    @JsonCreator
    public CityCode(@JsonProperty("ctprvnCd") Long ctprvnCd,
                    @JsonProperty("ctprvnNm") String ctprvnNm,
                    @JsonProperty("megaCodeId") Long megaCodeId) {
        this.ctprvnCd = ctprvnCd;
        this.ctprvnNm = ctprvnNm;
        this.megaCodeId = megaCodeId;
    }
}
