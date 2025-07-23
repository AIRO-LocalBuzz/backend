package backend.airo.persistence.product.entity;

import backend.airo.domain.Product.Product;
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
public class ProductEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    private String itemName;

    private String itemURL;

    private Long itemPrice;

    private String itemDescription;

    public ProductEntity(String itemName, String itemURL, Long itemPrice, String itemDescription) {
        this.itemName = itemName;
        this.itemURL = itemURL;
        this.itemPrice = itemPrice;
        this.itemDescription = itemDescription;
    }

    public void updateShopInfo(Product product) {
        this.itemName = product.getItemName();
        this.itemURL = product.getItemURL();
        this.itemPrice = product.getItemPrice();
        this.itemDescription = product.getItemDescription();
    }

    public static ProductEntity toEntity(Product aggregates) {
        return new ProductEntity(
                aggregates.getItemName(),
                aggregates.getItemURL(),
                aggregates.getItemPrice(),
                aggregates.getItemDescription()
        );
    }

    public static Product toDomain(ProductEntity aggregates) {
        return new Product(
                aggregates.id,
                aggregates.itemName,
                aggregates.itemURL,
                aggregates.itemPrice,
                aggregates.itemDescription
        );
    }
}
