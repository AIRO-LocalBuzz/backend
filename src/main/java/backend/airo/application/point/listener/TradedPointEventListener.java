package backend.airo.application.point.listener;

import backend.airo.application.point.usecase.TradedPointUseCase;
import backend.airo.domain.point_history.event.TradePointAddedEvent;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TradedPointEventListener {

    private final TradedPointUseCase tradedPointUseCase;


    @Description("포인트로 상품 구매시 구매 이력을 기록한다.")
    @Async
    @EventListener(TradePointAddedEvent.class)
    public void onInquiryAddedEvent(TradePointAddedEvent event) {
        tradedPointUseCase.saveTradePoint(
                event.getUserId(),
                event.getUsedPoint(),
                event.getItem_name(),
                event.getTradePointStatus()
        );
    }

}
