package backend.airo.domain.point_history.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.point_history.TradePoint;

import java.util.List;

public interface TradePointRepository extends AggregateSupport<TradePoint, Long> {

    List<TradePoint> getTradePointListByUserId(Long userId);

    Long getTradePointScore(Long userId);

}
