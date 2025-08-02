package backend.airo.domain.point.repository;

import backend.airo.domain.AggregateSupport;
import backend.airo.domain.point.Point;
import backend.airo.domain.point_history.PointHistory;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PointRepository extends AggregateSupport<Point, Long> {
    Optional<Point> findByUserId(Long userId);

    Long findPointByUserId(Long userId);

    void upsertIncrement(Long userId, long delta);
}
