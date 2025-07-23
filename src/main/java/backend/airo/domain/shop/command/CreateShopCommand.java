package backend.airo.domain.shop.command;

import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateShopCommand {

    private final ShopRepository shopRepository;

    public Shop handle(Shop shop) {
        return shopRepository.save(
                shop
        );
    }
}
