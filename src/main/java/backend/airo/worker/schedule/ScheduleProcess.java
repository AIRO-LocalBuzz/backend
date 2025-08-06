package backend.airo.worker.schedule;

import backend.airo.worker.schedule.shop.ShopService;
import backend.airo.worker.schedule.area_code.AreaCodeService;
import backend.airo.worker.schedule.rural_ex.RuralExService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@RequiredArgsConstructor
public class ScheduleProcess {

    private final AreaCodeService areaCodeService;
    private final ShopService shopService;
    private final RuralExService ruralExService;

    // 1월 1일, 7월 1일 새벽 2시 30분에 배치 작업 시작 [ 전국 농어촌 체험 휴양 마을 데이터 수집 ]
    @Scheduled(cron = "0 30 2 1 1,7 *", zone = "Asia/Seoul")
    public void getRural() {
        ruralExService.collectRuralExOf();
    }

    // 1월 1일, 7월 1일 새벽 2시 30분에 배치 작업 시작 [ 행정동,법정동 등 코드 수집 ]
    @Scheduled(cron = "0 30 2 1 1,7 *", zone = "Asia/Seoul")
    public void getAreaCode() {
        areaCodeService.collectCodeOf();
    }

    // 1월 1일, 7월 1일 새벽 3시 30분에 배치 작업 시작 [ 각 소상공인 상점 정보 수집 ]
    @Scheduled(cron = "0 30 3 1 1,7 *", zone = "Asia/Seoul")
    public void getAsyncShop() {
        shopService.collectShopOf();
    }
}
