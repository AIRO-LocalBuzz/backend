package backend.airo.domain.point.event;

import backend.airo.domain.point.TradePoint;
import backend.airo.domain.point.vo.TradePointStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TradePointAddedEvent {

    private final Long usedPoint;
    private final Long userId;
    private final String item_name;
    private final TradePointStatus tradePointStatus;

    public TradePointAddedEvent(TradePoint tradePoint) {
        this.usedPoint = tradePoint.getUsedPoint();
        this.userId = tradePoint.getUserId();
        this.item_name = tradePoint.getItem_name();
        this.tradePointStatus = tradePoint.getTradePointStatus();
    }
}
