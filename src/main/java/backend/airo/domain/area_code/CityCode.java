package backend.airo.domain.area_code;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CityCode {

    private final Long ctprvnCd;

    private final String ctprvnNm;

    private final Long megaCodeId;

    @JsonCreator
    public CityCode(@JsonProperty("ctprvnCd") Long ctprvnCd,
                    @JsonProperty("ctprvnNm") String ctprvnNm,
                    @JsonProperty("megaCodeId") Long megaCodeId) {
        this.ctprvnCd = ctprvnCd;
        this.ctprvnNm = ctprvnNm;
        this.megaCodeId = megaCodeId;
    }
}
