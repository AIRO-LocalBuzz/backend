package backend.airo.batch.ClureFatvl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClutrFatvlJob {

    private final ClutrFatvlService clutrFatvlService;

    // 매월 1일 새벽 2시 30분에 배치 작업 시작 [ 각 지역별 문화, 축제 데이터 수집 ]
    @Scheduled(cron = "0 30 2 1 * *", zone = "Asia/Seoul")
    public void syncMonthly() {
        YearMonth target = YearMonth.now(); // 이번 달
        log.info("Starting monthly festival data sync for {}", target);
        clutrFatvlService.collectFestivalOf(target);
    }

    // 테스트용 (즉시 실행)
    @EventListener(ApplicationReadyEvent.class)
    public void syncOnce() {
        YearMonth target = YearMonth.now();
        clutrFatvlService.collectFestivalOf(target);
    }
}