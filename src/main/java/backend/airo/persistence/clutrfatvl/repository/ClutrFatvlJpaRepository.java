package backend.airo.persistence.clutrfatvl.repository;

import backend.airo.persistence.clutrfatvl.entity.ClutrFatvlEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClutrFatvlJpaRepository extends JpaRepository<ClutrFatvlEntity, Long> {

    Page<ClutrFatvlEntity> findByAddress_MegaCodeIdAndAddress_CtprvnCodeId(String regionCtprvnCd, String regionSignguCd, Pageable pageable);
}
