package backend.airo.infra.open_api.rural_ex.adapter;

import backend.airo.domain.rural_ex.RuralEx;
import backend.airo.domain.rural_ex.port.RuralDataPort;
import backend.airo.domain.rural_ex.vo.RuralExAddress;
import backend.airo.domain.rural_ex.vo.RuralExAdmin;
import backend.airo.domain.rural_ex.vo.RuralExLocation;
import backend.airo.infra.open_api.rural_ex.client.OpenApiRuralExFeignClient;
import backend.airo.infra.open_api.rural_ex.dto.OpenApiRuralExResponse;
import backend.airo.infra.open_api.rural_ex.vo.RuralExInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RuralExAdapter implements RuralDataPort {

    private final OpenApiRuralExFeignClient openApiRuralExFeignClient;

    @Override
    public List<RuralEx> getRuralExInfo() {
        OpenApiRuralExResponse<RuralExInfo> clutrFatvlInfo = openApiRuralExFeignClient.getClutrFatvlInfo("1", "10000");
        List<RuralExInfo> exprnVillageInfos = clutrFatvlInfo.items();
        return exprnVillageInfos.stream().map(list ->
                new RuralEx(
                        0L,
                        list.exprnSe(),
                        list.exprnCn(),
                        list.holdFclty(),
                        list.exprnAr(),
                        list.exprnPicUrl(),
                        list.exprnVilageNm(),
                        list.ctprvnNm(),
                        list.signguNm(),
                        list.rprsntvNm(),
                        list.phoneNumber(),
                        new RuralExAddress(list.rdnmadr(), list.lnmadr()),
                        new RuralExAdmin(list.appnDate(), list.institutionNm(), list.insttCode(), list.insttNm()),
                        new RuralExLocation(list.latitude(), list.longitude()),
                        list.referenceDate())
        ).toList();
    }

}
