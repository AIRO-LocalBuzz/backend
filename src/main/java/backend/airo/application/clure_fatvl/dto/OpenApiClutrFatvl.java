package backend.airo.application.clure_fatvl.dto;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record OpenApiClutrFatvl(
        Long contentId,
        Integer contenttypeId,
        String title,
        Double lat,
        Double lon,
        String addr1,
        String addr2,
        Integer megaCodeId,
        Integer ctprvnCodeId,
        String phoneNumber,
        String cat1,
        String firstImage,
        String firstImage2,
        @JsonFormat(pattern = "yyyyMMddHHmmss")
        LocalDate modifiedDate
        ) {
}
