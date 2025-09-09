package backend.airo.api.clutr_fatvl.dto;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ClutrFatvListResponse(

        Long contentId,
        String title,
        String firstImage1,
        String firstImage2

) {

    public static ClutrFatvListResponse create(ClutrFatvl clutrFatvl, String megaName, String cityName) {
        return ClutrFatvListResponse.builder()
                .contentId(clutrFatvl.getContentId())
                .title(clutrFatvl.getTitle())
                .firstImage1(clutrFatvl.getFirstImage())
                .firstImage2(clutrFatvl.getFirstImage2())
                .build();
    }

}
