package backend.airo.domain.shop.query;

import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetShopQuery {

    private final ShopRepository shopRepository;

    public Shop handle(Long shopId) {
        return shopRepository.findById(
                shopId
        );
    }

}
