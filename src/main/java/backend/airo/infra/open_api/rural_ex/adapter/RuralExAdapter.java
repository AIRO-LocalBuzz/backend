package backend.airo.infra.open_api.rural_ex.adapter;

import backend.airo.domain.rural_ex.port.RuralDataPort;
import backend.airo.infra.open_api.rural_ex.client.OpenApiRuralExFeignClient;
import backend.airo.infra.open_api.rural_ex.dto.OpenApiRuralExResponse;
import backend.airo.infra.open_api.rural_ex.vo.ExprnVillageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RuralExAdapter implements RuralDataPort {

    private final OpenApiRuralExFeignClient openApiRuralExFeignClient;

    public void getClutrFatvlInfo() {
        OpenApiRuralExResponse<ExprnVillageInfo> clutrFatvlInfo = openApiRuralExFeignClient.getClutrFatvlInfo("1", "10000");
        List<ExprnVillageInfo> exprnVillageInfos = clutrFatvlInfo.items();

    }

}
