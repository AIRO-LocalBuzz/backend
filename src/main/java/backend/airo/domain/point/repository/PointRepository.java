package backend.airo.domain.point.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.point.Point;

import java.util.List;

public interface PointRepository extends AggregateSupport<Point, Long> {

    List<Point> getPointListByUserId(Long userId);

    Long getPointScoreByUserId(Long userId);

}
