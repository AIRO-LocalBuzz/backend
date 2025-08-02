package backend.airo.batch.cure_fatvl;

import backend.airo.domain.area_code.command.DeleteAllClutrFatvlByDateCommand;
import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.domain.clure_fatvl.command.CreateAllClutrFatvlCommand;
import backend.airo.support.ServerStartupNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClutrFatvlService {

    private final CreateAllClutrFatvlCommand createAllClutrFatvlCommand;
    private final DeleteAllClutrFatvlByDateCommand deleteAllClutrFatvlByDateCommand;
    private final AsyncClutrFatvlDataCollector asyncClutrFatvlDataCollector;
    private final ServerStartupNotifier serverStartupNotifier;

    public void collectFestivalOf(){
        LocalDate start = LocalDate.now();  // 오늘
        LocalDate sixMonthsLater = LocalDate.now().plusMonths(6);
        YearMonth endMonth = YearMonth.of(sixMonthsLater.getYear(), sixMonthsLater.getMonth());
        LocalDate end = endMonth.atEndOfMonth();  // 6개월 뒤의 말일

        // 각 날짜별로 비동기 작업 생성 (다른 클래스의 @Async 메서드 호출)
        List<CompletableFuture<List<ClutrFatvl>>> futures = new ArrayList<>();
        log.info("지역 문화 축제 데이터 수집 시작 : 연도 | " + start);
        for (LocalDate day = start; !day.isAfter(end); day = day.plusDays(1)) {
            CompletableFuture<List<ClutrFatvl>> future =
                    asyncClutrFatvlDataCollector.fetchAllByStartDateAsync(day);
            futures.add(future);
        }
        log.info("지역 문화 축제 데이터 수집 완료");
        // 모든 비동기 작업 완료 대기 및 결과 수집
        List<ClutrFatvl> allEntities = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        log.info("allEntities.size() :: " + allEntities.size());

        if (!allEntities.isEmpty()) {
            deleteAllClutrFatvlByDateCommand.handle(start, end);
            int size = createAllClutrFatvlCommand.handle(allEntities);
            serverStartupNotifier.collectClutrFatvlDataSuccessWithNotification(size, start, end);
        } else {
            serverStartupNotifier.collectClutrFatvlDataFailWithNotification();
        }
    }

}