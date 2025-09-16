package backend.airo.infra.open_api.tour.vo;

import com.fasterxml.jackson.annotation.*;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KoreaTourShoplInfo(
        @JsonProperty("addr1")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String addr1,

        @JsonProperty("addr2")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String addr2,

        @JsonProperty("areacode")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String areacode,

        @JsonProperty("cat1")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String cat1,

        @JsonProperty("cat2")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String cat2,

        @JsonProperty("cat3")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String cat3,

        @JsonProperty("contentid")
        long contentid,

        @JsonProperty("contenttypeid")
        int contenttypeid,

        @JsonProperty("createdtime")
        @JsonFormat(pattern = "yyyyMMddHHmmss")
        LocalDateTime createdtime,

        @JsonProperty("firstimage")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String firstimage,

        @JsonProperty("firstimage2")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String firstimage2,

        @JsonProperty("cpyrhtDivCd")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String cpyrhtDivCd,

        @JsonProperty("mapx")
        double mapx,

        @JsonProperty("mapy")
        double mapy,

        @JsonProperty("mlevel")
        int mlevel,

        @JsonProperty("modifiedtime")
        @JsonFormat(pattern = "yyyyMMddHHmmss")
        LocalDateTime modifiedtime,

        @JsonProperty("sigungucode")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String sigungucode,

        @JsonProperty("tel")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String tel,

        @JsonProperty("title")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String title,

        @JsonProperty("zipcode")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String zipcode,

        @JsonProperty("lDongRegnCd")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String lDongRegnCd,

        @JsonProperty("lDongSignguCd")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String lDongSignguCd,

        @JsonProperty("lclsSystm1")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String lclsSystm1,

        @JsonProperty("lclsSystm2")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String lclsSystm2,

        @JsonProperty("lclsSystm3")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String lclsSystm3
) {

}
