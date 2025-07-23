package backend.airo.domain.Product.command;

import backend.airo.domain.Product.Product;
import backend.airo.domain.Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateProductCommand {

    private final ProductRepository productRepository;

    public Product handle(Product product, Long productId) {
        Product findProductInfo = productRepository.findById(productId);
        Product updateProductInfo = findProductInfo.updateShopInfo(product);
        return productRepository.save(updateProductInfo);
    }
}
