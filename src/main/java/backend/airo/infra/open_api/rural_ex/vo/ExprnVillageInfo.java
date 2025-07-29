package backend.airo.infra.open_api.rural_ex.vo;

import com.fasterxml.jackson.annotation.*;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExprnVillageInfo(

        @JsonProperty("exprnVilageNm")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String exprnVilageNm,

        @JsonProperty("ctprvnNm")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String ctprvnNm,

        @JsonProperty("signguNm")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String signguNm,

        @JsonProperty("exprnSe")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String exprnSe,

        @JsonProperty("exprnCn")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String exprnCn,

        @JsonProperty("holdFclty")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String holdFclty,

        @JsonProperty("exprnAr")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String exprnAr,

        @JsonProperty("exprnPicUrl")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String exprnPicUrl,

        @JsonProperty("rdnmadr")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String rdnmadr,

        @JsonProperty("lnmadr")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String lnmadr,

        @JsonProperty("rprsntvNm")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String rprsntvNm,

        @JsonProperty("phoneNumber")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String phoneNumber,

        @JsonProperty("appnDate")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate appnDate,

        @JsonProperty("homepageUrl")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String homepageUrl,

        @JsonProperty("institutionNm")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String institutionNm,

        @JsonProperty("latitude")
        double latitude,

        @JsonProperty("longitude")
        double longitude,

        @JsonProperty("referenceDate")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate referenceDate,

        @JsonProperty("insttCode")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String insttCode,

        @JsonProperty("insttNm")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        String insttNm

) {}
