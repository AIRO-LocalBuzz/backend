package backend.airo.api.init;

import backend.airo.worker.schedule.area_code.AreaCodeService;
import backend.airo.worker.schedule.rural_ex.RuralExService;
import backend.airo.worker.schedule.shop.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/init/")
@RequiredArgsConstructor
public class initController {

    private final AreaCodeService areaCodeService;
    private final ShopService shopService;
    private final RuralExService ruralExService;


    @GetMapping("all/data")
    public void initControl1() {
        areaCodeService.collectCodeOf();
        shopService.collectShopOf();
    }

    @GetMapping("shop/data")
    public void initControl3() {
        shopService.collectShopOf();
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
