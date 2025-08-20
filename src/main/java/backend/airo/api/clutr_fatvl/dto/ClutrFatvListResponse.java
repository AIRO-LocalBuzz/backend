package backend.airo.api.clutr_fatvl.dto;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ClutrFatvListResponse(

        String id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        String region,
        String place,
        String shortDesc,
        boolean progressCheck,
        boolean periodCheck,
        boolean ended

) {

    public static ClutrFatvListResponse create(ClutrFatvl clutrFatvl, String megaName, String cityName) {
        return ClutrFatvListResponse.builder()
                .id(clutrFatvl.id())
                .name(clutrFatvl.fstvlNm())
                .startDate(clutrFatvl.period().start())
                .endDate(clutrFatvl.period().end())
                .region(addMegaNameCityName(megaName, cityName))
                .place(clutrFatvl.opar())
                .progressCheck(clutrFatvl.period().progressCheck())
                .periodCheck(clutrFatvl.period().periodCheck())
                .ended(clutrFatvl.period().ended())
                .build();
    }

    private static String addMegaNameCityName(String megaName, String cityName) {
        return megaName + " " + cityName;
    }

}
