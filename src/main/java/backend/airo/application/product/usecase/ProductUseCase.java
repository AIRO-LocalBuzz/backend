package backend.airo.application.product.usecase;

import backend.airo.domain.Product.Product;
import backend.airo.domain.Product.command.CreateProductCommand;
import backend.airo.domain.Product.command.DeleteProductCommand;
import backend.airo.domain.Product.command.UpdateProductCommand;
import backend.airo.domain.Product.query.GetProductInfoQuery;
import backend.airo.domain.Product.query.GetProductListQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductUseCase {

    private final CreateProductCommand createProductCommand;
    private final UpdateProductCommand updateProductCommand;
    private final DeleteProductCommand deleteProductCommand;
    private final GetProductInfoQuery getProductInfoQuery;
    private final GetProductListQuery getProductListQuery;

    public Product saveProduct(String itemName, String itemURL, Long itemPrice, String itemDescription) {
        return createProductCommand.handle(
                new Product(
                        0L,
                        itemName,
                        itemURL,
                        itemPrice,
                        itemDescription
                )
        );
    }

    public Product updateProduct(String itemName, String itemURL, Long itemPrice, String itemDescription, Long shopId) {
        return updateProductCommand.handle(
                new Product(
                        shopId,
                        itemName,
                        itemURL,
                        itemPrice,
                        itemDescription
                ), shopId
        );
    }

    public void deleteProduct(Long shopId) {
        deleteProductCommand.handle(shopId);
    }

    public List<Product> getProductList() {
        return getProductListQuery.handle();
    }

    public Product getProductInfo(Long shopId) {
        return getProductInfoQuery.handle(shopId);
    }

}
