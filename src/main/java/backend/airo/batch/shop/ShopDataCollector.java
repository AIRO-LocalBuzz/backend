package backend.airo.batch.shop;

import backend.airo.domain.area_code.MegaCode;
import backend.airo.domain.shop.command.CreateAllShopCommand;
import backend.airo.domain.shop.dto.ShopPage;
import backend.airo.domain.shop.port.OpenApiShopPort;
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
        int pageNo = 1;
        boolean hasNext = true;

        while (hasNext) {
            ShopPage shopPage = openApiShopPort.getShopData(
                    String.valueOf(pageNo),
                    "10000",
                    "ctprvnCd",
                    megaCode.getCtprvnCd()
            );

            if (shopPage.items().isEmpty()) break;

            createAllShopCommand.handle(shopPage.items());

            int totalPages = (int) Math.ceil((double) shopPage.totalCount() / shopPage.numOfRows());
            pageNo++;

            hasNext = pageNo <= totalPages;
        }
    }
}
