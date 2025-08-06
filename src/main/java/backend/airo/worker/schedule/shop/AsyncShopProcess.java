package backend.airo.worker.schedule.shop;

import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.area_code.query.GetMegaAllCodeQuery;
import backend.airo.domain.shop.query.DeleteAllShopCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AsyncShopProcess {

    private final GetMegaAllCodeQuery getMegaAllCodeQuery;
    private final ShopDataCollector shopDataCollector;
    private final DeleteAllShopCommand deleteAllShopCommand;

    @Async("apiTaskExecutor")
    //추후에 Upsert방식을 고려
    public void getShopDataList() {
        deleteAllShopCommand.handle();
        List<MegaCode> regions = getMegaAllCodeQuery.handle();
        for (MegaCode megaCode : regions) {
            shopDataCollector.processRegion(megaCode);
        }
    }
}
