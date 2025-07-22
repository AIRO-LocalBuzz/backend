package backend.airo.persistence.point.adapter;

import backend.airo.domain.point.Point;
import backend.airo.domain.point.TradePoint;
import backend.airo.domain.point.repository.PointRepository;
import backend.airo.domain.point.repository.TradePointRepository;
import backend.airo.persistence.point.entity.PointEntity;
import backend.airo.persistence.point.entity.TradePointEntity;
import backend.airo.persistence.point.repository.PointJpaRepository;
import backend.airo.persistence.point.repository.TradePointJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class TradePointAdapter implements TradePointRepository {

    private final TradePointJpaRepository tradePointJpaRepository;

    @Override
    public TradePoint save(TradePoint aggregate) {
        TradePointEntity entity = TradePointEntity.toEntity(aggregate);
        TradePointEntity savePointEntity = tradePointJpaRepository.save(entity);
        return TradePointEntity.toDomain(savePointEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public TradePoint findById(Long aggregates) {
        TradePointEntity pointEntity = tradePointJpaRepository.findById(aggregates).orElseThrow(() ->
                new IllegalArgumentException("Point Not Found with id - " + aggregates)
        );
        return TradePointEntity.toDomain(pointEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TradePoint> getPointListByUserId(Long userId) {
        List<TradePointEntity> pointEntitiesByUserId = tradePointJpaRepository.getTradePointEntitiesByUserId(userId);
        if (pointEntitiesByUserId.isEmpty()) {
            return List.of();
        }
        return pointEntitiesByUserId.stream().map(TradePointEntity::toDomain).toList();
    }

    //TODO 현재는 쓰지 않음.
    @Override
    public Collection<TradePoint> saveAll(Collection<TradePoint> aggregates) {
        return List.of();
    }
}
