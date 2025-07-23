package backend.airo.domain.shop.command;

import backend.airo.domain.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteShopCommand {

    private final ShopRepository shopRepository;

    public void handle(Long shopId) {
        shopRepository.deleteByShopId(shopId);
    }

}
