package backend.airo.application.clure_fatvl.dto;

import java.time.LocalDate;

public record OpenApiClutrFatvlInfo(
        String fstvlNm,
        String opar,
        String fstvlCo,
        LocalDate start,
        LocalDate end,
        Double lat,
        Double lon,
        String road,
        String lot,
        String mnnstNm,
        String auspcInsttNm,
        String suprtInsttNm,
        String phoneNumber,
        String homepageUrl,
        String relateInfo,
        LocalDate referenceDate,
        String insttCode,
        String insttNm

) {
}
