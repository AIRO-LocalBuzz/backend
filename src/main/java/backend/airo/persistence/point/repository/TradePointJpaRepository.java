package backend.airo.persistence.point.repository;

import backend.airo.persistence.point.entity.TradePointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TradePointJpaRepository extends JpaRepository<TradePointEntity, Long> {

    List<TradePointEntity> getTradePointEntitiesByUserId(Long userId);

    @Query("SELECT SUM(tp.useedPoint) FROM TradePointEntity tp WHERE tp.userId = :userId")
    Long getPointScoreByUserId(Long userId);

}
