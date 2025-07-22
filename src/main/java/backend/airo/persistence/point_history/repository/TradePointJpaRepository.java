package backend.airo.persistence.point_history.repository;

import backend.airo.persistence.point_history.entity.TradePointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TradePointJpaRepository extends JpaRepository<TradePointEntity, Long> {

    List<TradePointEntity> getTradePointEntitiesByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(tp.usedPoint), 0) FROM TradePointEntity tp WHERE tp.userId = :userId AND tp.tradePointStatus = 'SUCCESS'")
    Long getPointScoreByUserId(Long userId);

}
