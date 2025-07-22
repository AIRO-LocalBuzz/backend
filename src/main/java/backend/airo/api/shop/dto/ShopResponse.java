package backend.airo.api.shop.dto;

import backend.airo.domain.shop.Shop;


public record ShopResponse(
        Long id,
        String itemName,
        String itemURL,
        Long itemPrice,
        String itemDescription
) {
    public static ShopResponse from(Shop shop) {
        return new ShopResponse(shop.getId(), shop.getItemName(), shop.getItemURL(), shop.getItemPrice(), shop.getItemDescription());
    }
}
