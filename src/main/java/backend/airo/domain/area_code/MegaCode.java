package backend.airo.domain.area_code;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public record MegaCode(
        Long ctprvnCd,
        String ctprvnNm) {
    @JsonCreator
    public MegaCode(@JsonProperty("ctprvnCd") Long ctprvnCd,
                    @JsonProperty("ctprvnNm") String ctprvnNm) {
        this.ctprvnCd = ctprvnCd;
        this.ctprvnNm = ctprvnNm;
    }
}
