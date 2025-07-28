package backend.airo.domain.shop.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.shop.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShopRepository extends AggregateSupport<Shop, Long> {

    Page<Shop> findAll(String megaName, String cityName, Pageable pageable);

    void deleteAll();
}