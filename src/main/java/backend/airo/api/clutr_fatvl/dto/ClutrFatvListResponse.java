package backend.airo.api.clutr_fatvl.dto;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ClutrFatvListResponse(

        Long id,
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
                .id(clutrFatvl.getId())
                .name(clutrFatvl.getFstvlNm())
                .startDate(clutrFatvl.getPeriod().start())
                .endDate(clutrFatvl.getPeriod().end())
                .region(addMegaNameCityName(megaName, cityName))
                .place(clutrFatvl.getOpar())
                .progressCheck(clutrFatvl.getPeriod().progressCheck())
                .periodCheck(clutrFatvl.getPeriod().periodCheck())
                .ended(clutrFatvl.getPeriod().ended())
                .build();
    }

    private static String addMegaNameCityName(String megaName, String cityName) {
        return megaName + " " + cityName;
    }

}
