package backend.airo.persistence.point_history.repository;

import backend.airo.persistence.point_history.entity.PointHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistoryEntity, Long> {

    List<PointHistoryEntity> getPointEntitiesByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(p.point), 0) FROM PointHistoryEntity p WHERE p.userId = :userId")
    Long getPointScoreByUserId(Long userId);
}
