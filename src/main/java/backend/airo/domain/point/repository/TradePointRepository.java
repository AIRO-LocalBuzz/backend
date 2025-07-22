package backend.airo.domain.point.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.point.Point;
import backend.airo.domain.point.TradePoint;

import java.lang.annotation.Target;
import java.util.List;

public interface TradePointRepository extends AggregateSupport<TradePoint, Long> {

    List<TradePoint> getPointListByUserId(Long userId);

}
