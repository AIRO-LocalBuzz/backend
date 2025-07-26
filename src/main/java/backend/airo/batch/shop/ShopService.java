package backend.airo.batch.shop;

import backend.airo.support.TimeCatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopService {

    private final ShopDataCollector shopDataCollector;
    private final TimeCatch timeCatch = new TimeCatch("ShopService Time Check");

    public void collectShopOf() {
        timeCatch.start();
        shopDataCollector.getShopDataList();
        timeCatch.end();
    }

}
