package backend.airo.domain.shop.query;

import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetShopListQuery {

    private final ShopRepository shopRepository;

    public List<Shop> handle() {
        return shopRepository.findAll();
    }

}
