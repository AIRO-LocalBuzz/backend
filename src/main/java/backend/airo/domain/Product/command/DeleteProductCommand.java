package backend.airo.domain.Product.command;

import backend.airo.domain.Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteProductCommand {

    private final ProductRepository productRepository;

    public void handle(Long productId) {
        productRepository.deleteByShopId(productId);
    }

}
