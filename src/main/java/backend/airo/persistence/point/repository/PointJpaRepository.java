package backend.airo.persistence.point.repository;

import backend.airo.persistence.point.entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointJpaRepository extends JpaRepository<PointEntity, Long> { }
