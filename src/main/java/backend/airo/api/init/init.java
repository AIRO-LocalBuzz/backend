package backend.airo.api.init;

import backend.airo.batch.area_code.AreaCodeService;
import backend.airo.batch.cure_fatvl.ClutrFatvlService;
import backend.airo.batch.rural_ex.RuralExService;
import backend.airo.batch.shop.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@Slf4j
@RestController
@RequestMapping("/v1/init/")
@RequiredArgsConstructor
public class init {

    private final ClutrFatvlService clutrFatvlService;
    private final AreaCodeService areaCodeService;
    private final ShopService shopService;
    private final RuralExService ruralExService;


    @GetMapping("all/data")
    public void initControl1() {
        clutrFatvlService.collectFestivalOf();
        areaCodeService.collectCodeOf();
        shopService.collectShopOf();
    }

    @GetMapping("clutrFatvl/data")
    public void initControl2() {
        log.info("지역 문화 축제 데이터 수집 요청");
        clutrFatvlService.collectFestivalOf();
    }

    @GetMapping("shop/data")
    public void initControl3() {
        clutrFatvlService.collectFestivalOf();
    }


    @GetMapping("all/data/area_code")
    public void initControl4() {
        areaCodeService.collectCodeOf();
    }

    @GetMapping("rural_ex/data")
    public void initControl5() {
        ruralExService.collectRuralExOf();
    }

}
