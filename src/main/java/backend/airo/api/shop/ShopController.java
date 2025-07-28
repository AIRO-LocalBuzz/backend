package backend.airo.api.shop;

import backend.airo.api.global.dto.Response;
import backend.airo.api.global.swagger.ShopControllerSwagger;
import backend.airo.api.shop.dto.ShopInfoResponse;
import backend.airo.api.shop.dto.ShopListResponse;
import backend.airo.application.shop.usecase.ShopUseCase;
import backend.airo.domain.shop.Shop;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "Shop", description = "전국 문화, 축제, 이벤트 관련 API V1")
public class ShopController implements ShopControllerSwagger {

    private final ShopUseCase shopUseCase;

    @Override
    @GetMapping("/shop")
    public Response<List<ShopListResponse>> getShoplList(
            @RequestParam() String megaCode,
            @RequestParam() String cityCode
    ) {
        List<Shop> shopList = shopUseCase.getShopList(megaCode, cityCode);

        return Response.success(shopList.stream().map(list ->
                new ShopListResponse(
                        list.getId(),
                        list.getShopName(),
                        list.getAddress().lot(),
                        list.getAddress().road(),
                        list.getShopType().getTypeName()
                )
        ).toList());
    }

    @Override
    @GetMapping("/shop/info")
    public Response<ShopInfoResponse> getShopInfo(
            @RequestParam String shopId) {
        Shop shop = shopUseCase.getShopInfo(Long.valueOf(shopId));
        return Response.success(ShopInfoResponse.create(shop));
    }

}
