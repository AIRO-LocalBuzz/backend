package backend.airo.persistence.shop.adapter;

import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.repository.ShopRepository;
import backend.airo.persistence.shop.entity.ShopEntity;
import backend.airo.persistence.shop.repository.ShopJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class ShopAdapter implements ShopRepository {

    private final ShopJpaRepository shopJpaRepository;

    @Override
    public Shop save(Shop aggregate) {
        ShopEntity shopEntity = shopJpaRepository.findById(aggregate.getId())
                .map(getShopEntity -> {
                    getShopEntity.updateShopInfo(aggregate);
                    return getShopEntity;
                }).orElseGet(() -> ShopEntity.toEntity(aggregate));
        ShopEntity saveShopEntity = shopJpaRepository.save(shopEntity);
        return ShopEntity.toDomain(saveShopEntity);
    }

    @Override
    public Shop findById(Long aggregates) {
        ShopEntity shopEntity = shopJpaRepository.findById(aggregates).orElseThrow(() ->
                new IllegalArgumentException("Shop Not Found with id - " + aggregates)
        );
        return ShopEntity.toDomain(shopEntity);
    }

    @Override
    public List<Shop> findAll() {
        List<ShopEntity> shopEntities = shopJpaRepository.findAll();
        if (shopEntities.isEmpty()) {
            return List.of();
        }
        return shopEntities.stream().map(ShopEntity::toDomain).toList();
    }

    @Override
    public void deleteByShopId(Long shopId) {
        shopJpaRepository.deleteById(shopId);
    }


    @Override
    public List<Shop> saveAll(Collection<Shop> aggregates) {
        return List.of();
    }

}
