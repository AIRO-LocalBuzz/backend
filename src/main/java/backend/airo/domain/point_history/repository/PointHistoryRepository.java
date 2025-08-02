package backend.airo.domain.point_history.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.point_history.PointHistory;

import java.util.List;

public interface PointHistoryRepository extends AggregateSupport<PointHistory, Long> {

    List<PointHistory> getPointListByUserId(Long userId);

    Long getPointScoreByUserId(Long userId);

}
