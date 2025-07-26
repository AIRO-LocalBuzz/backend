package backend.airo.api.init;

import backend.airo.batch.area_code.AreaCodeService;
import backend.airo.batch.cure_fatvl.ClutrFatvlService;
import backend.airo.batch.shop.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("/v1/init/")
@RequiredArgsConstructor
public class init {

    private final ClutrFatvlService clutrFatvlService;
    private final AreaCodeService areaCodeService;
    private final ShopService shopService;


    @GetMapping("all/data")
    public void initControl1() {
        YearMonth target = YearMonth.now();
        clutrFatvlService.collectFestivalOf(target);
        areaCodeService.collectCodeOf();
        shopService.collectShopOf();
    }


    @GetMapping("all/data/area_code")
    public void initControl2() {
        areaCodeService.collectCodeOf();
    }

}
