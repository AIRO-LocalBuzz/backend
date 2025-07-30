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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenApiRuralExAdapter implements RuralDataPort {

    private final OpenApiRuralExFeignClient openApiRuralExFeignClient;

    @Override
    public List<RuralEx> getRuralExInfo() {
        final int pageSize = 100;
        int page = 1;

        List<RuralEx> allData = new ArrayList<>();

        while (true) {
            OpenApiRuralExResponse<RuralExInfo> response = openApiRuralExFeignClient.getClutrFatvlInfo(String.valueOf(page), String.valueOf(pageSize));
            List<RuralExInfo> items = response.items();

            if (items == null || items.isEmpty()) break;

            List<RuralEx> mapped = items.stream()
                    .map(list -> new RuralEx(
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
                            list.referenceDate()
                    ))
                    .toList();

            allData.addAll(mapped);

            int totalCount = Integer.parseInt(response.response().body().totalCount());
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);

            if (page >= totalPages) break;

            page++;
        }

        return allData;
    }


}
