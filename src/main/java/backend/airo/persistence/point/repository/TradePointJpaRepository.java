package backend.airo.persistence.point.repository;

import backend.airo.persistence.point.entity.PointEntity;
import backend.airo.persistence.point.entity.TradePointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradePointJpaRepository extends JpaRepository<TradePointEntity, Long> {

    List<TradePointEntity> getTradePointEntitiesByUserId(Long userId);

}
