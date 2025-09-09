package backend.airo.persistence.clutrfatvl.repository;

import backend.airo.persistence.clutrfatvl.entity.ClutrFatvlInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClutrFatvlInfoJpaRepository extends JpaRepository<ClutrFatvlInfoEntity, Long> {
}
