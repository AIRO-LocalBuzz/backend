package backend.airo.domain.shop.port;

import backend.airo.domain.shop.dto.ShopPage;

public interface OpenApiShopPort {

    ShopPage getShopData(String pageNo, String contentTypeId, String megaCode);

}