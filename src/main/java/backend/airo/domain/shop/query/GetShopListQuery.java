package backend.airo.domain.shop.query;

import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GetShopListQuery {

    private final ShopRepository shopRepository;

    public Page<Shop> handle(String megaName, String cityName, Pageable pageable) {
        return shopRepository.findAll(megaName, cityName, pageable);
    }

}
