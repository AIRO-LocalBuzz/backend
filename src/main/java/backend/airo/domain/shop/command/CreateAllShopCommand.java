package backend.airo.domain.shop.command;

import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateAllShopCommand {

    private final ShopRepository shopRepository;

    public void handle(List<Shop> shops) {
        shopRepository.saveAll(shops);
    }

}
