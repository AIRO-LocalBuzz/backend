package backend.airo.domain.shop.dto;

import backend.airo.domain.shop.Shop;

import java.util.List;

public record ShopPage(
        List<Shop> items,
        Integer numOfRows,
        Integer pageNo,
        Long totalCount
) {
}
