package backend.airo.infra.open_api.tour.client;

import backend.airo.infra.open_api.rural_ex.config.OpenApiFeignClientConfiguration;
import backend.airo.infra.open_api.tour.dto.OpenApiShopResponse;
import backend.airo.infra.open_api.tour.vo.ShopInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 소상공인진흥공단에서 제공하는 Open API를 이용한 시,군,구 [ 행정 구역 ] 상가 정보 수집
 * <a href="https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15012005">...</a>
 */
@FeignClient(
        name = "openApi-shop",
        url = "https://apis.data.go.kr/",
        configuration = OpenApiFeignClientConfiguration.class)
public interface OpenApiShopFeignClient {

    @GetMapping("/B553077/api/open/sdsc2/storeListInDong")
    OpenApiShopResponse<List<ShopInfo>> getShopInfo(
            //페이지 번호
            @RequestParam(value = "pageNo") String pageNo,
            //한 페이지 결과수,
            @RequestParam(value = "numOfRows") String numOfRows,
            //시도는 ctprvnCd, 시군구는 signguCd, 행정동은 adongCd 코드 사용
            @RequestParam(value = "divId") String divId,
            //시도 코드값 or 시군구 코드값 or 행정동은 행정동 코드 값
            @RequestParam(value = "key") String key
//            @RequestParam(value = "indsLclsCd") String indsLclsCd,
//            @RequestParam(value = "indsMclsCd") String indsMclsCd,
//            @RequestParam(value = "indsSclsCd") String indsSclsCd
            );

}
