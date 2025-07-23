package backend.airo.domain.Product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Product {

    private Long id = 0L;
    private String itemName;
    private String itemURL;
    private Long itemPrice;
    private String itemDescription;

    public Product(Long id, String itemName, String itemURL, Long itemPrice, String itemDescription) {
        this.id = id;
        this.itemName = itemName;
        this.itemURL = itemURL;
        this.itemPrice = itemPrice;
        this.itemDescription = itemDescription;
    }

    public Product updateShopInfo(Product product) {
        this.id = product.id;
        this.itemName = product.itemName;
        this.itemURL = product.itemURL;
        this.itemPrice = product.itemPrice;
        this.itemDescription = product.itemDescription;
        return this;
    }
}
