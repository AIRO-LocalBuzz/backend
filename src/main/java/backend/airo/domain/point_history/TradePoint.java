package backend.airo.domain.point_history;


import backend.airo.domain.point_history.vo.TradePointStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TradePoint{

    private final Long id;
    private final Long usedPoint;
    private final Long userId;
    private final String item_name;
    private TradePointStatus tradePointStatus;
    private final LocalDateTime exchangedAt;

    public TradePoint(Long id, Long usedPoint, Long userId, String item_name, TradePointStatus tradePointStatus, LocalDateTime exchangedAt) {
        this.id = id;
        this.usedPoint = usedPoint;
        this.userId = userId;
        this.item_name = item_name;
        this.tradePointStatus = tradePointStatus;
        this.exchangedAt = exchangedAt;
    }

    public static TradePoint create(Long itemPoint, Long userId, String item_name) {
        return new TradePoint(
                0L,
                itemPoint,
                userId,
                item_name,
                null,
                null
        );
    }


    public void markSuccess() {
        tradePointStatus = TradePointStatus.SUCCESS;
    }

    public void markFailure(TradePointStatus tradePointStatus) {
        this.tradePointStatus = tradePointStatus;
    }
}
