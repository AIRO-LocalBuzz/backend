//package backend.airo.infra.open_api.clure_fatvl.vo;
//
//import com.fasterxml.jackson.annotation.*;
//
//import java.time.LocalDate;
//
//@JsonIgnoreProperties(ignoreUnknown = true)
//public record ClutrFatvlInfo(
//        @JsonProperty("fstvlNm")
//        @JsonSetter(nulls = Nulls.AS_EMPTY)
//        String fstvlNm,
//
//        @JsonProperty("opar")
//        @JsonSetter(nulls = Nulls.AS_EMPTY)
//        String opar,
//
//        @JsonProperty("fstvlStartDate")
//        @JsonFormat(pattern = "yyyy-MM-dd")
//        LocalDate fstvlStartDate,
//
//        @JsonProperty("fstvlEndDate")
//        @JsonFormat(pattern = "yyyy-MM-dd")
//        LocalDate fstvlEndDate,
//
//        @JsonProperty("fstvlCo")
//        @JsonSetter(nulls = Nulls.AS_EMPTY)
//        String fstvlCo,
//
//        @JsonProperty("mnnstNm")
//        @JsonSetter(nulls = Nulls.AS_EMPTY)
//        String mnnstNm,
//
//        @JsonProperty("auspcInsttNm")
//        @JsonSetter(nulls = Nulls.AS_EMPTY)
//        String auspcInsttNm,
//
//        @JsonProperty("suprtInsttNm")
//        @JsonSetter(nulls = Nulls.AS_EMPTY)
//        String suprtInsttNm,
//
//        @JsonProperty("phoneNumber")
//        @JsonSetter(nulls = Nulls.AS_EMPTY)
//        String phoneNumber,
//
//        @JsonProperty("homepageUrl")
//        @JsonSetter(nulls = Nulls.AS_EMPTY)
//        String homepageUrl,
//
//        @JsonProperty("relateInfo")
//        @JsonSetter(nulls = Nulls.AS_EMPTY)
//        String relateInfo,
//
//        @JsonProperty("rdnmadr")
//        @JsonSetter(nulls = Nulls.AS_EMPTY)
//        String rdnmadr,
//
//        @JsonProperty("lnmadr")
//        @JsonSetter(nulls = Nulls.AS_EMPTY)
//        String lnmadr,
//
//        @JsonProperty("latitude")
//        Double latitude,
//
//        @JsonProperty("longitude")
//        Double longitude,
//
//        @JsonProperty("referenceDate")
//        @JsonFormat(pattern = "yyyy-MM-dd")
//        LocalDate referenceDate,
//
//        @JsonProperty("insttCode")
//        @JsonSetter(nulls = Nulls.AS_EMPTY)
//        String insttCode,
//
//        @JsonProperty("insttNm")
//        @JsonSetter(nulls = Nulls.AS_EMPTY)
//        String insttNm
//) {
//        public ClutrFatvlInfo {
//                latitude  = latitude  == null ? 0.0 : latitude;
//                longitude = longitude == null ? 0.0 : longitude;
//        }
//
//}
