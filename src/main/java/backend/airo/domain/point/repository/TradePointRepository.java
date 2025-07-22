package backend.airo.domain.point.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.point.TradePoint;

import java.util.List;

public interface TradePointRepository extends AggregateSupport<TradePoint, Long> {

    List<TradePoint> getTradePointListByUserId(Long userId);

    Long getTradePointScore(Long userId);

}
