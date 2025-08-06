package backend.airo.worker.schedule.shop;

import backend.airo.support.TimeCatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopService {

    private final AsyncShopProcess asyncShopProcess;
    private final TimeCatch timeCatch = new TimeCatch("ShopService Time Check");

    public void collectShopOf() {
        timeCatch.start();
        asyncShopProcess.getShopDataList();
        timeCatch.end();
    }

}
