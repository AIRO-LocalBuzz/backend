package backend.airo.infra.open_api.tour.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KoreaTourFatvl(
        @JsonProperty("addr1") String addr1,
        @JsonProperty("addr2") String addr2,
        @JsonProperty("areacode") int areaCode,
        @JsonProperty("cat1") String cat1,
        @JsonProperty("cat2") String cat2,
        @JsonProperty("cat3") String cat3,
        @JsonProperty("contentid") long contentId,
        @JsonProperty("contenttypeid") int contentTypeId,
        @JsonProperty("createdtime")
        @JsonFormat(pattern = "yyyyMMddHHmmss") LocalDateTime createdTime,
        @JsonProperty("firstimage") String firstImage,
        @JsonProperty("firstimage2") String firstImage2,
        @JsonProperty("cpyrhtDivCd") String copyrightDivCd,
        @JsonProperty("mapx") double mapX,
        @JsonProperty("mapy") double mapY,
        @JsonProperty("mlevel") int mlevel,
        @JsonProperty("modifiedtime")
        @JsonFormat(pattern = "yyyyMMddHHmmss") LocalDateTime modifiedTime,
        @JsonProperty("sigungucode") int sigunguCode,
        @JsonProperty("tel") String tel,
        @JsonProperty("title") String title,
        @JsonProperty("zipcode") String zipcode, // 유지: 선행 0 보존
        @JsonProperty("lDongRegnCd") int lDongRegnCd,
        @JsonProperty("lDongSignguCd") int lDongSignguCd,
        @JsonProperty("lclsSystm1") String lclsSystm1,
        @JsonProperty("lclsSystm2") String lclsSystm2,
        @JsonProperty("lclsSystm3") String lclsSystm3
) {
}
