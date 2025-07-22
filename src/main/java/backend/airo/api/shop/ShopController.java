package backend.airo.api.shop;

import backend.airo.api.example.dto.TestResponse;
import backend.airo.api.global.dto.Response;
import backend.airo.api.shop.dto.ShopRequest;
import backend.airo.api.shop.dto.ShopResponse;
import backend.airo.application.shop.usecase.ShopUseCase;
import backend.airo.domain.example.Test;
import backend.airo.domain.shop.Shop;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "Shop", description = "Point Shop 관련 API")
public class ShopController {

    private final ShopUseCase shopUseCase;

    @PostMapping("/shop")
    public Response<ShopResponse> saveShop(@RequestBody ShopRequest shopRequest) {
        Shop saveShopItem = shopUseCase.saveShopItem(shopRequest.itemName(), shopRequest.itemURL(), shopRequest.itemPrice(), shopRequest.itemDescriptionOrEmpty());
        return Response.success(ShopResponse.from(saveShopItem));
    }

    @PatchMapping("/shop")
    public Response<ShopResponse> updateShopInfo(
            @RequestParam Long shopId,
            @RequestBody ShopRequest shopRequest) {
        Shop saveShopItem = shopUseCase.updateShopItem(shopRequest.itemName(), shopRequest.itemURL(), shopRequest.itemPrice(), shopRequest.itemDescriptionOrEmpty(), shopId);
        return Response.success(ShopResponse.from(saveShopItem));
    }

    @GetMapping("/shop")
    public Response<List<ShopResponse>> getShopList() {
        List<Shop> getShopItemList = shopUseCase.getShopItemList();
        return Response.success(getShopItemList.stream().map(ShopResponse::from).toList());
    }

    @GetMapping("/shop/Info")
    public Response<ShopResponse> getShopInfo(@RequestParam Long shopId) {
        Shop getShopInfo = shopUseCase.getShopInfo(shopId);
        return Response.success(ShopResponse.from(getShopInfo));
    }

    @DeleteMapping("/shop")
    public Response<Void> deleteShop(@RequestParam Long shopId) {
        shopUseCase.deleteShopItem(shopId);
        return Response.success();
    }






}
