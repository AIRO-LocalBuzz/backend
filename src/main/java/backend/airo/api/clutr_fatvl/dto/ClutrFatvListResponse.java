package backend.airo.api.clutr_fatvl.dto;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

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
//        String thumbnailUrl

) {

    public static List<ClutrFatvListResponse> create(List<ClutrFatvl> clutrFatvlLis) {
        return clutrFatvlLis.stream().map(list ->
                ClutrFatvListResponse.builder()
                        .name(list.getFstvlNm())
                        .startDate(list.getPeriod().start())
                        .endDate(list.getPeriod().end())
                        .region(null)
                        .place(list.getOpar())
                        .progressCheck(list.getPeriod().progressCheck())
                        .periodCheck(list.getPeriod().periodCheck())
                        .ended(list.getPeriod().ended())
                        .build()
        ).toList();
    }


}
