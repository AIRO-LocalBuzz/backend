package backend.airo.infra.open_api.tour.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KoreaTourFatvlInfo(
        @JsonProperty("contentid") long contentId,
        @JsonProperty("contenttypeid") int contentTypeId,

        @JsonProperty("sponsor1") String sponsor1,
        @JsonProperty("sponsor1tel") String sponsor1Tel,
        @JsonProperty("sponsor2") String sponsor2,
        @JsonProperty("sponsor2tel") String sponsor2Tel,

        @JsonProperty("eventstartdate")
        @JsonFormat(pattern = "yyyyMMdd") LocalDate eventStartDate,

        @JsonProperty("eventenddate")
        @JsonFormat(pattern = "yyyyMMdd") LocalDate eventEndDate,

        @JsonProperty("playtime") String playTime,
        @JsonProperty("eventplace") String eventPlace,
        @JsonProperty("eventhomepage") String eventHomepage,
        @JsonProperty("agelimit") String ageLimit,
        @JsonProperty("bookingplace") String bookingPlace,
        @JsonProperty("placeinfo") String placeInfo,
        @JsonProperty("subevent") String subEvent,
        @JsonProperty("program") String program,

        @JsonProperty("usetimefestival") String useTimeFestival,
        @JsonProperty("discountinfofestival") String discountInfoFestival,
        @JsonProperty("spendtimefestival") String spendTimeFestival,
        @JsonProperty("festivalgrade") String festivalGrade,

        @JsonProperty("progresstype") String progressType,
        @JsonProperty("festivaltype") String festivalType
) {
}
