package backend.airo.domain.Product.query;

import backend.airo.domain.Product.Product;
import backend.airo.domain.Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetProductInfoQuery {

    private final ProductRepository productRepository;

    public Product handle(Long shopId) {
        return productRepository.findById(
                shopId
        );
    }
}
