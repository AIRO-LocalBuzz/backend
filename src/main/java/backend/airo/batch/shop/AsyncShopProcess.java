package backend.airo.batch.shop;

import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.query.GetMegaAllCodeQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AsyncShopProcess {

    private final GetMegaAllCodeQuery getMegaAllCodeQuery;
    private final ShopDataCollector shopDataCollector;

//    @Async("apiTaskExecutor")
    public void getShopDataList() {
        List<MegaCode> regions = getMegaAllCodeQuery.handle();
        for (MegaCode megaCode : regions) {
            shopDataCollector.processRegion(megaCode);
        }
    }
}
