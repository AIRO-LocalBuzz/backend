package backend.airo.domain.shop;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Shop {

    private Long id = 0L;
    private String itemName;
    private String itemURL;
    private Long itemPrice;
    private String itemDescription;

    public Shop(Long id, String itemName, String itemURL, Long itemPrice, String itemDescription) {
        this.id = id;
        this.itemName = itemName;
        this.itemURL = itemURL;
        this.itemPrice = itemPrice;
        this.itemDescription = itemDescription;
    }

    public Shop updateShopInfo(Shop shop) {
        this.id = shop.id;
        this.itemName = shop.itemName;
        this.itemURL = shop.itemURL;
        this.itemPrice = shop.itemPrice;
        this.itemDescription = shop.itemDescription;
        return this;
    }
}
