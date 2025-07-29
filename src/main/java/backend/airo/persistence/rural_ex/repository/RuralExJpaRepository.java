package backend.airo.persistence.rural_ex.repository;

import backend.airo.persistence.rural_ex.entity.RuralExEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuralExJpaRepository extends JpaRepository<RuralExEntity, Long> {

    Page<RuralExEntity> findByCtprvnNmAndSignguNm(String ctprvnNm, String signguNm, Pageable pageable);

}
