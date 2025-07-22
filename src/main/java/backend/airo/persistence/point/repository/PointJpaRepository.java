package backend.airo.persistence.point.repository;

import backend.airo.persistence.point.entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointJpaRepository extends JpaRepository<PointEntity, Long> {

    List<PointEntity> getPointEntitiesByUserId(Long userId);

}
