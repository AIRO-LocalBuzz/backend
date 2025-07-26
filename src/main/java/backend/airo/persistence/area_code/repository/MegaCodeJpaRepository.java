package backend.airo.persistence.area_code.repository;

import backend.airo.persistence.area_code.entity.MegaCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MegaCodeJpaRepository extends JpaRepository<MegaCodeEntity, Long> {
}
