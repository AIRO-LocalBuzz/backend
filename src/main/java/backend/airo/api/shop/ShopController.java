package backend.airo.api.shop;

import backend.airo.api.global.dto.Response;
import backend.airo.api.global.swagger.ShopControllerSwagger;
import backend.airo.api.shop.dto.ShopInfoResponse;
import backend.airo.api.shop.dto.ShopListResponse;
import backend.airo.application.shop.usecase.ShopUseCase;
import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.domain.shop.Shop;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
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

//    @Override
//    @GetMapping("/shop")
//    public Response<List<ShopListResponse>> getShoplList(
//            @RequestParam(defaultValue = "1") String end,
//            @RequestParam(defaultValue = "10") String numOfRows,
//            @RequestParam(defaultValue = "") String divId
//    ) {
//        List<Shop> shopList = shopUseCase.getShopList(end, numOfRows, divId);
//        return Response.success(shopList.stream().map(list -> new ShopListResponse(list.getId(), list)));
//    }

    @Override
    @GetMapping("/shop/info")
    public Response<ShopInfoResponse> getShopInfo(
            @RequestParam String shopId) {
        Shop shop = shopUseCase.getShopInfo(Long.valueOf(shopId));
        return Response.success(ShopInfoResponse.create(shop));
    }

}
