package backend.airo.persistence.rural_ex.repository;

import backend.airo.persistence.rural_ex.entity.RuralExEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuralExJpaRepository extends JpaRepository<RuralExEntity, Long> {
}
