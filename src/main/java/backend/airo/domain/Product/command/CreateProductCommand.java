package backend.airo.domain.Product.command;

import backend.airo.domain.Product.Product;
import backend.airo.domain.Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateProductCommand {

    private final ProductRepository productRepository;

    public Product handle(Product product) {
        return productRepository.save(
                product
        );
    }
}
