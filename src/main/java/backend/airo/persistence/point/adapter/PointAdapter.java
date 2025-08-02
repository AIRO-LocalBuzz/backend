package backend.airo.persistence.point.adapter;

import backend.airo.domain.point.Point;
import backend.airo.domain.point.repository.PointRepository;
import backend.airo.domain.point_history.repository.PointHistoryRepository;
import backend.airo.persistence.point.entity.PointEntity;
import backend.airo.persistence.point.repository.PointJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class PointAdapter implements PointRepository {

    private final PointJpaRepository pointJpaRepository;

    @Override
    public Point save(Point aggregate) {
        return null;
    }

    @Override
    public Optional<Point> findByUserId(Long userId) {
        return pointJpaRepository.findById(userId)
                .map(PointEntity::toDomain);
    }

    @Override
    public Long findPointByUserId(Long userId) {
        return pointJpaRepository.findByUserId(userId).getPointScore();
    }

    @Override
    public void upsertIncrement(Long userId, long point) {
        pointJpaRepository.upsertIncrement(userId, point);
    }

    @Override
    public Collection<Point> saveAll(Collection<Point> aggregates) {
        return List.of();
    }

    @Override
    public Point findById(Long aLong) {
        return null;
    }


}
