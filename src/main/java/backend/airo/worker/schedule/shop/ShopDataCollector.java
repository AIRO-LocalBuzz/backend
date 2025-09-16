package backend.airo.worker.schedule.shop;

import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.shop.command.CreateAllShopCommand;
import backend.airo.domain.shop.dto.ShopPage;
import backend.airo.domain.shop.port.OpenApiShopPort;
import backend.airo.worker.schedule.shop.vo.ContentTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShopDataCollector {

    private final OpenApiShopPort openApiShopPort;
    private final CreateAllShopCommand createAllShopCommand;

    @Async("apiTaskExecutor")
    public void processRegion(MegaCode megaCode) {
        for (ContentTypeId value : ContentTypeId.values()) {
            boolean hasNext = true;
            int pageNo = 1;
            while (hasNext) {
                ShopPage shopPage = openApiShopPort.getShopData(
                        String.valueOf(pageNo),
                        value.getTypeId(),
                        String.valueOf(megaCode.getCtprvnCd())
                );

                if (shopPage.items().isEmpty()) break;

                createAllShopCommand.handle(shopPage.items());

                int totalPages = (int) Math.ceil((double) shopPage.totalCount() / shopPage.numOfRows());
                pageNo++;

                hasNext = pageNo <= totalPages;
            }
        }
    }
}
