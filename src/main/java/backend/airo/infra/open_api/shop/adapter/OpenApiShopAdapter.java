package backend.airo.infra.open_api.shop.adapter;

import backend.airo.domain.shop.dto.ShopPage;
import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.port.OpenApiShopPort;
import backend.airo.domain.shop.vo.*;
import backend.airo.infra.open_api.shop.client.OpenApiShopFeignClient;
import backend.airo.infra.open_api.shop.dto.OpenApiShopResponse;
import backend.airo.infra.open_api.shop.vo.ShopInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenApiShopAdapter implements OpenApiShopPort {

    private final OpenApiShopFeignClient openApiShopFeignClient;

    public ShopPage getShopData(String pageNo, String numOfRows, String divId, String key) {
        OpenApiShopResponse<List<ShopInfo>> openApiShopInfo = openApiShopFeignClient.getShopInfo(pageNo, numOfRows, divId, key);
        List<Shop> shops = openApiShopInfo.getItems().stream()
                .filter(info -> isValidShopType(info.indsLclsCd()))
                .map(list ->
                new Shop(
                        list.bizesNm(),
                        new IndustryCodes(list.ksicCd(), list.indsLclsCd(), list.indsMclsCd(), list.indsSclsCd()),
                        new RegionCodes(list.ctprvnCd(), list.signguCd(), list.adongCd(), list.ldongCd()),
                        new ShopAddress(list.rdnmAdr(), list.lnoAdr(), list.newZipcd()),
                        new ShopGeoPoint(list.lat(), list.lon()),
                        new FloorInfo(list.flrNo(), list.hoNo()),
                        ShopType.valueOf(list.indsLclsCd()),
                        list.brchNm()
                )
        ).toList();
        OpenApiShopResponse.Body<List<ShopInfo>> body = openApiShopInfo.body();
        return new ShopPage(shops, body.numOfRows(), body.pageNo(), body.totalCount());
    }

    private boolean isValidShopType(String code) {
        try {
            ShopType.valueOf(code);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
