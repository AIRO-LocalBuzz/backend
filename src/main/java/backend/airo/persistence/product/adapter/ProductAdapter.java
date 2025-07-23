package backend.airo.persistence.product.adapter;

import backend.airo.domain.Product.Product;
import backend.airo.domain.Product.repository.ProductRepository;
import backend.airo.persistence.product.entity.ProductEntity;
import backend.airo.persistence.product.repository.ProductJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class ProductAdapter implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product save(Product aggregate) {
        ProductEntity productEntity = productJpaRepository.findById(aggregate.getId())
                .map(getProductEntity -> {
                    getProductEntity.updateShopInfo(aggregate);
                    return getProductEntity;
                }).orElseGet(() -> ProductEntity.toEntity(aggregate));
        ProductEntity saveProductEntity = productJpaRepository.save(productEntity);
        return ProductEntity.toDomain(saveProductEntity);
    }

    @Override
    public Product findById(Long aggregates) {
        ProductEntity productEntity = productJpaRepository.findById(aggregates).orElseThrow(() ->
                new IllegalArgumentException("Shop Not Found with id - " + aggregates)
        );
        return ProductEntity.toDomain(productEntity);
    }

    @Override
    public List<Product> findAll() {
        List<ProductEntity> shopEntities = productJpaRepository.findAll();
        if (shopEntities.isEmpty()) {
            return List.of();
        }
        return shopEntities.stream().map(ProductEntity::toDomain).toList();
    }

    @Override
    public void deleteByShopId(Long shopId) {
        productJpaRepository.deleteById(shopId);
    }


    @Override
    public List<Product> saveAll(Collection<Product> aggregates) {
        return List.of();
    }

}
