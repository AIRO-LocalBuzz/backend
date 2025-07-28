package backend.airo.api.shop.dto;

import lombok.Builder;

@Builder
public record ShopListResponse(

        Long id,
        String name,
        String lot,
        String road,
        String indeScleName
) {


}
