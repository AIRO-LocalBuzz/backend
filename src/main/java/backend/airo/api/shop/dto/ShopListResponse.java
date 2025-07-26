package backend.airo.api.shop.dto;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ShopListResponse(

        Long id,
        String name,
        String region,
        String place,
        String shortDesc,
        boolean progressCheck,
        boolean periodCheck,
        boolean ended
//        String thumbnailUrl

) {

//    public static List<ShopListResponse> create(List<ClutrFatvl> clutrFatvlLis) {
//        return clutrFatvlLis.stream().map(list ->
//                ShopListResponse.builder()
//                        .name(list.getFstvlNm())
//                        .startDate(list.getPeriod().start())
//                        .endDate(list.getPeriod().end())
//                        .region(null)
//                        .place(list.getOpar())
//                        .progressCheck(list.getPeriod().progressCheck())
//                        .periodCheck(list.getPeriod().periodCheck())
//                        .ended(list.getPeriod().ended())
//                        .build()
//        ).toList();
//    }


}
