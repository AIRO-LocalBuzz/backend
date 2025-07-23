package backend.airo.domain.Product.query;

import backend.airo.domain.Product.Product;
import backend.airo.domain.Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetProductListQuery {

    private final ProductRepository productRepository;

    public List<Product> handle() {
        return productRepository.findAll();
    }

}
