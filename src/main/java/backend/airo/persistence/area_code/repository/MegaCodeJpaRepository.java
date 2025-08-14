package backend.airo.persistence.area_code.repository;

import backend.airo.persistence.area_code.entity.MegaCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MegaCodeJpaRepository extends JpaRepository<MegaCodeEntity, Long> {

    MegaCodeEntity findByCtprvnCd(Long ctprvnCd);

    Optional<MegaCodeEntity> findByCtprvnNm(String ctprvnNm);

}
