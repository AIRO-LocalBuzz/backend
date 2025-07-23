package backend.airo.batch;

import backend.airo.infra.clure_fatvl_open_api.client.OpenApiFeignClient;
import backend.airo.infra.clure_fatvl_open_api.dto.OpenApiResponse;
import backend.airo.infra.clure_fatvl_open_api.vo.ClutrFatvlInfo;
import backend.airo.persistence.clutrfatvl.adapter.ClutrFatvlAdapter;
import backend.airo.persistence.clutrfatvl.entity.ClutrFatvlEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClutrFatvlSyncJob {

    private final OpenApiFeignClient openApiFeignClient;
    private final ClutrFatvlAdapter clutrFatvlAdapter;

    private final DateTimeFormatter YYYYMMDD = DateTimeFormatter.BASIC_ISO_DATE;

    //매월 1일 새벽 2시 30분에 배치 작업 시작 [ 각 지역별 문화, 축제 데이터 수집 ]
    @Scheduled(cron = "0 30 2 * * *")
    public void sync(){
        LocalDate today = LocalDate.now();
        LocalDate oneMonthLater = today.plusMonths(1);

        OpenApiResponse<List<ClutrFatvlInfo>> res =
                openApiFeignClient.getClutrFatvlInfo(today.format(YYYYMMDD), oneMonthLater.format(YYYYMMDD));

        List<ClutrFatvlInfo> item = res.getItems();

        List<ClutrFatvlEntity> clutrFatvlEntities = item.stream().map(ClutrFatvlEntity::create).toList();


        clutrFatvlAdapter.saveAll(clutrFatvlEntities);
    }
}
