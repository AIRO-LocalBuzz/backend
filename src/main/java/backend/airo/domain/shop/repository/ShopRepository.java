package backend.airo.domain.shop.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.shop.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShopRepository extends AggregateSupport<Shop, Long> {

    Page<Shop> findAll(String megaName, String cityName, String largeCategoryCode, String middleCategoryCode, String smallCategoryCode, Pageable pageable);

    void deleteAll();
}