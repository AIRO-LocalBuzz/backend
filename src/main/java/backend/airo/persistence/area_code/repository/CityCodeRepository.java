package backend.airo.persistence.area_code.repository;

import backend.airo.persistence.area_code.entity.CityCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityCodeRepository extends JpaRepository<CityCodeEntity, Long> {

    CityCodeEntity findByCtprvnCd(Long ctprvnCd);

}
