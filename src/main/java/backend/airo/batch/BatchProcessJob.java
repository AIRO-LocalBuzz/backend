package backend.airo.batch;

import backend.airo.batch.area_code.AreaCodeService;
import backend.airo.batch.cure_fatvl.ClutrFatvlService;
import backend.airo.batch.shop.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchProcessJob {

    private final ClutrFatvlService clutrFatvlService;
    private final AreaCodeService areaCodeService;
    private final ShopService shopService;

    // 1월 1일, 7월 1일 새벽 2시 30분에 배치 작업 시작 [ 행정동,법정동 등 코드 수집 ]
    @Scheduled(cron = "0 30 2 1 1,7 *", zone = "Asia/Seoul")
    public void syncAreaCode() {
        areaCodeService.collectCodeOf();
    }

    // 1월 1일, 7월 1일 새벽 3시 30분에 배치 작업 시작 [ 행정동,법정동 등 코드 수집 ]
    @Scheduled(cron = "0 30 3 1 1,7 *", zone = "Asia/Seoul")
    public void syncShop() {
        shopService.collectShopOf();
    }


    // 매일 새벽 2시 30분에 배치 작업 시작 [ 각 지역별 문화, 축제 데이터 수집 ]
    @Scheduled(cron = "0 30 2 * * *", zone = "Asia/Seoul")
    public void syncDailyFestival() {
        YearMonth target = YearMonth.now();
        clutrFatvlService.collectFestivalOf(target);
    }

}