package backend.airo.persistence.point_history.adapter;

import backend.airo.domain.point_history.PointHistory;
import backend.airo.domain.point_history.repository.PointHistoryRepository;
import backend.airo.persistence.point_history.entity.PointHistoryEntity;
import backend.airo.persistence.point_history.repository.PointHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class PointHistoryAdapter implements PointHistoryRepository {

    private final PointHistoryJpaRepository pointHistoryJpaRepository;

    @Override
    public PointHistory save(PointHistory aggregate) {
        PointHistoryEntity entity = PointHistoryEntity.toEntity(aggregate);
        PointHistoryEntity savePointHistoryEntity = pointHistoryJpaRepository.save(entity);
        return PointHistoryEntity.toDomain(savePointHistoryEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public PointHistory findById(Long aggregates) {
        PointHistoryEntity pointHistoryEntity = pointHistoryJpaRepository.findById(aggregates).orElseThrow(() ->
                new IllegalArgumentException("Point Not Found with id - " + aggregates)
        );
        return PointHistoryEntity.toDomain(pointHistoryEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PointHistory> getPointListByUserId(Long userId) {
        List<PointHistoryEntity> pointEntitiesByUserId = pointHistoryJpaRepository.getPointEntitiesByUserId(userId);
        if (pointEntitiesByUserId.isEmpty()) {
            return List.of();
        }
        return pointEntitiesByUserId.stream().map(PointHistoryEntity::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getPointScoreByUserId(Long userId) {
        return pointHistoryJpaRepository.getPointScoreByUserId(userId);
    }

    //TODO 현재는 쓰지 않음.
    @Override
    public Collection<PointHistory> saveAll(Collection<PointHistory> aggregates) {
        return List.of();
    }
}
