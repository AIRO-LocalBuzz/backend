package backend.airo.domain.Product.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.Product.Product;

import java.util.List;

public interface ProductRepository extends AggregateSupport<Product, Long> {

    List<Product> findAll();

    void deleteByShopId(Long shopId);

}
