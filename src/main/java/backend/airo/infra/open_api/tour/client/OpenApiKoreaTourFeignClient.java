package backend.airo.infra.open_api.tour.client;

import backend.airo.infra.open_api.tour.config.OpenApiFeignClientTourConfiguration;
import backend.airo.infra.open_api.tour.dto.OpenApiKoreaTourResponse;
import backend.airo.infra.open_api.tour.vo.KoreaTourFatvl;
import backend.airo.infra.open_api.tour.vo.KoreaTourFatvlInfo;
import backend.airo.infra.open_api.tour.vo.KoreaTourShoplInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 한국관광공사에서 제공하는 관광정보 서비스를 이용한 행사, 축제데이터 수집
 * <a href="https://www.data.go.kr/data/15101578/openapi.do">...</a>
 */
@FeignClient(
        name = "openApi-korea-tour",
        url = "https://apis.data.go.kr/B551011",
        configuration = OpenApiFeignClientTourConfiguration.class)
public interface OpenApiKoreaTourFeignClient {

    @GetMapping("/KorService2/areaBasedList2")
    OpenApiKoreaTourResponse<KoreaTourShoplInfo> getShopInfo(
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) String pageNo,
            @RequestParam(value = "contentTypeId") String contentId,
            @RequestParam(value = "lDongRegnCd") String megaId
            );


    @GetMapping("/KorService2/areaBasedList2")
    OpenApiKoreaTourResponse<KoreaTourFatvl> getClureFatvl(
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) String pageNo,
            @RequestParam(value = "contentTypeId") String contentId,
            @RequestParam(value = "modifiedtime") String modifiedtime
    );

    @GetMapping("/KorService2/detailIntro2")
    OpenApiKoreaTourResponse<KoreaTourFatvlInfo> getClureFatvlIntro(
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) String pageNo,
            @RequestParam(value = "contentId") String contentId,
            @RequestParam(value = "contentTypeId") String contentTypeId
    );

    @GetMapping("/KorService2/detailInfo2")
    OpenApiKoreaTourResponse<KoreaTourFatvlInfo> getClureFatvlInfo(
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) String pageNo,
            @RequestParam(value = "contentId") String contentId,
            @RequestParam(value = "contentTypeId") String contentTypeId
    );
}
