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
        String lot,
        String road,
        String indeScleName
) {


}
