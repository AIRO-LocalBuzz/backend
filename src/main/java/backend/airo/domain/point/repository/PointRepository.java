package backend.airo.domain.point.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.point.PointHistory;

import java.util.List;

public interface PointRepository extends AggregateSupport<PointHistory, Long> {

    List<PointHistory> getPointListByUserId(Long userId);

    Long getPointScoreByUserId(Long userId);

}
