package backend.airo.api.shop;

import backend.airo.api.global.dto.PageResponse;
import backend.airo.api.global.dto.Response;
import backend.airo.api.global.swagger.ShopControllerSwagger;
import backend.airo.api.shop.dto.ShopInfoResponse;
import backend.airo.api.shop.dto.ShopListResponse;
import backend.airo.application.shop.usecase.ShopUseCase;
import backend.airo.domain.shop.Shop;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Response<PageResponse<ShopListResponse>> getShoplList(
            @RequestParam() String megaCode,
            @RequestParam() String cityCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Shop> shopList = shopUseCase.getShopList(megaCode, cityCode, pageable);

        List<ShopListResponse> content = shopList.getContent().stream()
                .map(shop -> new ShopListResponse(
                        shop.getId(),
                        shop.getShopName(),
                        shop.getAddress().lot(),
                        shop.getAddress().road(),
                        shop.getShopType().getTypeName()
                ))
                .toList();
        return Response.success(
                new PageResponse<>(
                        content,
                        shopList.getNumber(),
                        shopList.getSize(),
                        shopList.getTotalElements(),
                        shopList.getTotalPages()
                )
        );
    }

    @Override
    @GetMapping("/shop/info")
    public Response<ShopInfoResponse> getShopInfo(
            @RequestParam String shopId) {
        Shop shop = shopUseCase.getShopInfo(Long.valueOf(shopId));
        return Response.success(ShopInfoResponse.create(shop));
    }

}
