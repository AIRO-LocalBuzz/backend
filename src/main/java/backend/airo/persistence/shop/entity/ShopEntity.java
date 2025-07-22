package backend.airo.persistence.shop.entity;

import backend.airo.domain.shop.Shop;
import backend.airo.persistence.abstracts.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ShopEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    private String itemName;

    private String itemURL;

    private Long itemPrice;

    private String itemDescription;

    public ShopEntity(String itemName, String itemURL, Long itemPrice, String itemDescription) {
        this.itemName = itemName;
        this.itemURL = itemURL;
        this.itemPrice = itemPrice;
        this.itemDescription = itemDescription;
    }

    public void updateShopInfo(Shop shop) {
        this.itemName = shop.getItemName();
        this.itemURL = shop.getItemURL();
        this.itemPrice = shop.getItemPrice();
        this.itemDescription = shop.getItemDescription();
    }

    public static ShopEntity toEntity(Shop aggregates) {
        return new ShopEntity(
                aggregates.getItemName(),
                aggregates.getItemURL(),
                aggregates.getItemPrice(),
                aggregates.getItemDescription()
        );
    }

    public static Shop toDomain(ShopEntity aggregates) {
        return new Shop(
                aggregates.id,
                aggregates.itemName,
                aggregates.itemURL,
                aggregates.itemPrice,
                aggregates.itemDescription
        );
    }
}
