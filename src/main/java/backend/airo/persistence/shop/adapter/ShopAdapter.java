package backend.airo.persistence.shop.adapter;

import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.repository.ShopRepository;
import backend.airo.persistence.shop.entity.ShopEntity;
import backend.airo.persistence.shop.repository.ShopJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ShopAdapter implements ShopRepository {

    private final ShopJpaRepository shopJpaRepository;

    @Override
    public Page<Shop> findAll(String megaName, String cityName, Pageable pageable) {
        Page<ShopEntity> shopEntities = shopJpaRepository.findByRegion_CtprvnCdAndRegion_SignguCd(megaName, cityName, pageable);
        if (shopEntities.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }
        return shopEntities.map(ShopEntity::toDomain);
    }

    @Override
    public void deleteAll() {
        shopJpaRepository.deleteAll();
    }

    @Override
    public Shop save(Shop aggregate) {
        return null;
    }

    @Override
    public Collection<Shop> saveAll(Collection<Shop> aggregates) {
        List<ShopEntity> shopEntity = aggregates.stream().map(ShopEntity::toEntity).toList();
        List<ShopEntity> saveShopEntities = shopJpaRepository.saveAll(shopEntity);
        return saveShopEntities.stream().map(ShopEntity::toDomain).toList();
    }

    @Override
    public Shop findById(Long shopId) {
        ShopEntity shopEntity = shopJpaRepository.findById(shopId).orElseThrow(() ->
                new IllegalArgumentException("Shop Not Found with id - " + shopId)
        );
        return ShopEntity.toDomain(shopEntity);
    }
}
