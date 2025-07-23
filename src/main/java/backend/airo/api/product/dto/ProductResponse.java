package backend.airo.api.product.dto;

import backend.airo.domain.Product.Product;


public record ProductResponse(
        Long id,
        String itemName,
        String itemURL,
        Long itemPrice,
        String itemDescription
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getItemName(), product.getItemURL(), product.getItemPrice(), product.getItemDescription());
    }
}
