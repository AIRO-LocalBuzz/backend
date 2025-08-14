package backend.airo.infra.open_api.clure_fatvl.adapter;

import backend.airo.application.clure_fatvl.dto.OpenApiClutrFatvlInfo;
import backend.airo.application.clure_fatvl.dto.OpenApiClutrFatvlResponse;
import backend.airo.domain.clure_fatvl.port.ClureFatvlPort;
import backend.airo.infra.open_api.clure_fatvl.client.OpenApiClureFatvlFeignClient;
import backend.airo.infra.open_api.clure_fatvl.dto.OpenApiClureFatvlResponse;
import backend.airo.infra.open_api.clure_fatvl.vo.ClutrFatvlInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenApiClureFatvlAdapter implements ClureFatvlPort {

    private final OpenApiClureFatvlFeignClient openApiClureFatvlFeignClient;

    public OpenApiClutrFatvlResponse getOpenApiClureFatvl(String page, String pageSize, String startDate) {
        OpenApiClureFatvlResponse<ClutrFatvlInfo> clutrFatvlInfo = openApiClureFatvlFeignClient.getClutrFatvlInfo(page, pageSize, startDate);
        return new OpenApiClutrFatvlResponse(
                clutrFatvlInfo.body() != null
                        ? clutrFatvlInfo.items().stream().map(list ->
                        new OpenApiClutrFatvlInfo(
                                list.fstvlNm(),
                                list.opar(),
                                list.fstvlCo(),
                                list.fstvlStartDate(),
                                list.fstvlEndDate(),
                                list.latitude(),
                                list.longitude(),
                                list.rdnmadr(),
                                list.lnmadr(),
                                list.mnnstNm(),
                                list.auspcInsttNm(),
                                list.suprtInsttNm(),
                                list.phoneNumber(),
                                list.homepageUrl(),
                                list.relateInfo(),
                                list.referenceDate(),
                                list.insttCode(),
                                list.insttNm()
                        )).toList()
                        : List.of(),
                clutrFatvlInfo.header().resultCode(),
                clutrFatvlInfo.body() != null ? clutrFatvlInfo.body().numOfRows() : String.valueOf(0),
                clutrFatvlInfo.body() != null ? clutrFatvlInfo.body().pageNo() : String.valueOf(0),
                clutrFatvlInfo.body() != null ? clutrFatvlInfo.body().totalCount() : String.valueOf(0)
        );
    }
}
