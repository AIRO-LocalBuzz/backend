package backend.airo.domain.clure_fatvl.port;

import backend.airo.application.clure_fatvl.dto.OpenApiClutrFatvlResponse;

public interface ClureFatvlPort {

    OpenApiClutrFatvlResponse getOpenApiClureFatvl(String page, String pageSize, String startDate);
}
