package backend.airo.batch;

import backend.airo.batch.area_code.AreaCodeService;
import backend.airo.batch.cure_fatvl.ClutrFatvlService;
import backend.airo.batch.rural_ex.RuralExService;
import backend.airo.batch.shop.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
//TODO 추후에 스프링 Batch로 바꿀 예정
public class BatchProcessJob {

    private final ClutrFatvlService clutrFatvlService;
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


    // 매일 아침 8시 30분에 배치 작업 시작 [ 각 지역별 문화, 축제 데이터 수집 ]
    @Scheduled(cron = "0 30 8 * * *", zone = "Asia/Seoul")
    public void getAsyncDailyFestival() {
        clutrFatvlService.collectFestivalOf();
    }

}