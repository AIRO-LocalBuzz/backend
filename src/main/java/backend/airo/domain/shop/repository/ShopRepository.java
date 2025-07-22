package backend.airo.domain.shop.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.example.Test;
import backend.airo.domain.shop.Shop;

import java.util.List;

public interface ShopRepository extends AggregateSupport<Shop, Long> {

    List<Shop> findAll();

    void deleteByShopId(Long shopId);

}
