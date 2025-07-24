package backend.airo.batch.ClureFatvl;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.domain.clure_fatvl.repository.ClutrFatvlRepository;
import backend.airo.persistence.clutrfatvl.adapter.ClutrFatvlAdapter;
import backend.airo.persistence.clutrfatvl.entity.ClutrFatvlEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
class ClutrFatvlService {

    private final ClutrFatvlRepository clutrFatvlRepository;
    private final ClutrFatvlAdapter clutrFatvlAdapter;
    private final AsyncFestivalDataCollector asyncFestivalDataCollector;

    public void collectFestivalOf(YearMonth ym) {
//        LocalDate start = ym.atDay(1);
//        LocalDate end = ym.atEndOfMonth();
        int year = ym.getYear();
        LocalDate start = LocalDate.of(year, 6, 1);        // 6월 1일
        LocalDate end   = LocalDate.of(year, 12, 31);      // 12월 31일

        log.info("Starting festival data collection for {} using async processing", ym);

        // 각 날짜별로 비동기 작업 생성 (다른 클래스의 @Async 메서드 호출)
        List<CompletableFuture<List<ClutrFatvl>>> futures = new ArrayList<>();

        for (LocalDate day = start; !day.isAfter(end); day = day.plusDays(1)) {
            CompletableFuture<List<ClutrFatvl>> future =
                    asyncFestivalDataCollector.fetchAllByStartDateAsync(day);
            futures.add(future);
        }

        // 모든 비동기 작업 완료 대기 및 결과 수집
        List<ClutrFatvl> allEntities = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());


        if (!allEntities.isEmpty()) {
            Collection<ClutrFatvl> clutrFatvls = clutrFatvlRepository.saveAll(allEntities);
            log.info("문화 축제 데이터 수집이 정상적으로 이루어 졌습니다. {}", clutrFatvls.size());
        }
    }
}