package backend.airo.infra.open_api.tour.adapter;

import backend.airo.application.clure_fatvl.dto.OpenApiClutrFatvl;
import backend.airo.application.clure_fatvl.dto.OpenApiClutrFatvlResponse;
import backend.airo.domain.clure_fatvl.port.ClureFatvlPort;
import backend.airo.domain.shop.Shop;
import backend.airo.domain.shop.dto.ShopPage;
import backend.airo.domain.shop.port.OpenApiShopPort;
import backend.airo.domain.shop.vo.*;
import backend.airo.infra.open_api.tour.client.OpenApiKoreaTourFeignClient;
import backend.airo.infra.open_api.tour.dto.OpenApiKoreaTourResponse;
import backend.airo.infra.open_api.tour.vo.KoreaTourFatvl;
import backend.airo.infra.open_api.tour.vo.KoreaTourShoplInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenApiTourAdapter implements OpenApiShopPort, ClureFatvlPort {

//    private final OpenApiShopFeignClient openApiShopFeignClient;
    private final OpenApiKoreaTourFeignClient openApiKoreaTourFeignClient;

//    public ShopPage getShopData(String pageNo, String numOfRows, String divId, String key) {
//        OpenApiShopResponse<List<ShopInfo>> openApiShopInfo = openApiShopFeignClient.getShopInfo(pageNo, numOfRows, divId, key);
//        List<Shop> shops = openApiShopInfo.getItems().stream()
//                .filter(info -> isValidShopType(info.indsLclsCd()))
//                .map(list ->
//                new Shop(
//                        list.bizesNm(),
//                        new IndustryCodes(list.ksicCd(), list.indsLclsCd(), list.indsMclsCd(), list.indsSclsCd()),
//                        new RegionCodes(list.ctprvnCd(), list.signguCd(), list.adongCd(), list.ldongCd()),
//                        new ShopAddress(list.rdnmAdr(), list.lnoAdr(), list.newZipcd()),
//                        new ShopGeoPoint(list.lat(), list.lon()),
//                        new FloorInfo(list.flrNo(), list.hoNo()),
//                        ShopType.valueOf(list.indsLclsCd()),
//                        list.brchNm()
//                )
//        ).toList();
//        OpenApiShopResponse.Body<List<ShopInfo>> body = openApiShopInfo.body();
//        return new ShopPage(shops, body.numOfRows(), body.pageNo(), body.totalCount());
//    }

//    private boolean isValidShopType(String code) {
//        try {
//            ShopType.valueOf(code);
//            return true;
//        } catch (IllegalArgumentException e) {
//            return false;
//        }
//    }


    @Override
    public ShopPage getShopData(String pageNo, String contentTypeId, String megaCode) {
        OpenApiKoreaTourResponse<KoreaTourShoplInfo> openApiShopInfo = openApiKoreaTourFeignClient.getShopInfo(pageNo, contentTypeId, megaCode);
        List<Shop> shops = openApiShopInfo.item().stream()
                .map(list ->
                        new Shop(
                                list.contentid(),
                                list.contenttypeid(),
                                list.title(),
                                new IndustryCodes(list.cat1(), list.cat2(), list.cat3()),
                                new RegionCodes(list.lDongRegnCd(), list.lDongRegnCd() + list.lDongSignguCd()),
                                new ShopAddress(list.addr1(), list.addr2()),
                                new ShopGeoPoint(list.mapy(), list.mapx()),
                                list.firstimage(),
                                list.firstimage2()
                        )
                ).toList();
        OpenApiKoreaTourResponse.Body<KoreaTourShoplInfo> body = openApiShopInfo.body();
        return new ShopPage(shops, body.numOfRows(), body.pageNo(), body.totalCount());
    }

    @Override
    public OpenApiClutrFatvlResponse getOpenApiClureFatvl(String page, String contentId, String modifiedtime) {
        OpenApiKoreaTourResponse<KoreaTourFatvl> openApiKoreaTourResponse = openApiKoreaTourFeignClient.getClureFatvl(page, contentId, modifiedtime);
        return new OpenApiClutrFatvlResponse(
                openApiKoreaTourResponse.body().items() != null
                ? openApiKoreaTourResponse.item().stream().map(list ->
                        new OpenApiClutrFatvl(
                                list.contentId(),
                                list.contentTypeId(),
                                list.title(),
                                list.mapY(),
                                list.mapX(),
                                list.addr1(),
                                list.addr2(),
                                list.lDongRegnCd(),
                                list.lDongRegnCd() + list.lDongSignguCd(),
                                list.tel(),
                                list.cat1(),
                                list.firstImage(),
                                list.firstImage2(),
                                list.modifiedTime().toLocalDate()
                                )).toList()
                :List.of(),
                openApiKoreaTourResponse.header().resultCode(),
                openApiKoreaTourResponse.body() != null ? String.valueOf(openApiKoreaTourResponse.body().numOfRows()) : String.valueOf(0),
                openApiKoreaTourResponse.body() != null ? String.valueOf(openApiKoreaTourResponse.body().pageNo()) : String.valueOf(0),
                openApiKoreaTourResponse.body() != null ? String.valueOf(openApiKoreaTourResponse.body().totalCount()) : String.valueOf(0)
        );
    }
}
