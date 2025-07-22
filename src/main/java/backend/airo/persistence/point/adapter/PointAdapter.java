package backend.airo.persistence.point.adapter;

import backend.airo.domain.point.Point;
import backend.airo.domain.point.repository.PointRepository;
import backend.airo.persistence.point.entity.PointEntity;
import backend.airo.persistence.point.repository.PointJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class PointAdapter implements PointRepository {

    private final PointJpaRepository pointJpaRepository;

    @Override
    public Point save(Point aggregate) {
        PointEntity entity = PointEntity.toEntity(aggregate);
        PointEntity savePointEntity = pointJpaRepository.save(entity);
        return PointEntity.toDomain(savePointEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Point findById(Long aggregates) {
        PointEntity pointEntity = pointJpaRepository.findById(aggregates).orElseThrow(() ->
                new IllegalArgumentException("Point Not Found with id - " + aggregates)
        );
        return PointEntity.toDomain(pointEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Point> getPointListByUserId(Long userId) {
        List<PointEntity> pointEntitiesByUserId = pointJpaRepository.getPointEntitiesByUserId(userId);
        if (pointEntitiesByUserId.isEmpty()) {
            return List.of();
        }
        return pointEntitiesByUserId.stream().map(PointEntity::toDomain).toList();
    }

    @Override
    public Long getPointScoreByUserId(Long userId) {
        return pointJpaRepository.getPointScoreByUserId(userId);
    }

    //TODO 현재는 쓰지 않음.
    @Override
    public Collection<Point> saveAll(Collection<Point> aggregates) {
        return List.of();
    }
}
