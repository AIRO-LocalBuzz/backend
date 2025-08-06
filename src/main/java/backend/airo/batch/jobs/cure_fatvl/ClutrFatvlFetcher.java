package backend.airo.batch.jobs.cure_fatvl;

import backend.airo.application.clure_fatvl.dto.OpenApiClutrFatvlResponse;
import backend.airo.domain.clure_fatvl.port.ClureFatvlPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ClutrFatvlFetcher {

    private final ClureFatvlPort clureFatvlPort;

    public OpenApiClutrFatvlResponse fetchClutrFatvl(String page, String pageSize, String startDate) {
        return clureFatvlPort.getOpenApiClureFatvl(page, pageSize, startDate);
    }
}
