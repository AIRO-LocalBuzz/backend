package backend.airo.application.shop.usecase;

import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.command.CreateShopCommand;
import backend.airo.domain.shop.command.DeleteShopCommand;
import backend.airo.domain.shop.command.UpdateShopCommand;
import backend.airo.domain.shop.query.GetShopInfoQuery;
import backend.airo.domain.shop.query.GetShopListQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShopUseCase {

    private final CreateShopCommand createShopCommand;
    private final UpdateShopCommand updateShopCommand;
    private final DeleteShopCommand deleteShopCommand;
    private final GetShopInfoQuery getShopInfoQuery;
    private final GetShopListQuery getShopListQuery;

    public Shop saveShopItem(String itemName, String itemURL, Long itemPrice, String itemDescription) {
        return createShopCommand.handle(
                new Shop(
                        0L,
                        itemName,
                        itemURL,
                        itemPrice,
                        itemDescription
                )
        );
    }

    public Shop updateShopItem(String itemName, String itemURL, Long itemPrice, String itemDescription, Long shopId) {
        return updateShopCommand.handle(
                new Shop(
                        shopId,
                        itemName,
                        itemURL,
                        itemPrice,
                        itemDescription
                ), shopId
        );
    }

    public void deleteShopItem(Long shopId) {
        deleteShopCommand.handle(shopId);
    }

    public List<Shop> getShopItemList() {
        return getShopListQuery.handle();
    }

    public Shop getShopInfo(Long shopId) {
        return getShopInfoQuery.handle(shopId);
    }

}
