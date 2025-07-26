package backend.airo.batch.shop;

import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.query.GetMegaCodeQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShopDataCollector {

    private final GetMegaCodeQuery getMegaCodeQuery;
    private final AsyncShopDataCollector asyncShopDataCollector;

    @Async("apiTaskExecutor")
    public void getShopDataList() {
        List<MegaCode> regions = getMegaCodeQuery.handle();
        for (MegaCode megaCode : regions) {
            asyncShopDataCollector.processRegion(megaCode);
        }
    }
}
