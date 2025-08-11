package backend.airo.domain.area_code;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MegaCode {
    private final Long ctprvnCd;
    private final String ctprvnNm;

    @JsonCreator
    public MegaCode(@JsonProperty("ctprvnCd") Long ctprvnCd,
                    @JsonProperty("ctprvnNm") String ctprvnNm) {
        this.ctprvnCd = ctprvnCd;
        this.ctprvnNm = ctprvnNm;
    }
}
