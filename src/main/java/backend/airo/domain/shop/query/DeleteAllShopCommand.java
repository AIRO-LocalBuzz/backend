package backend.airo.domain.shop.query;

import backend.airo.domain.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteAllShopCommand {

    private final ShopRepository shopRepository;

    public void handle() {
        shopRepository.deleteAll();
    }

}
