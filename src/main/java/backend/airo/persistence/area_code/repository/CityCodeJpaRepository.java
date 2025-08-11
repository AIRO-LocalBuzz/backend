package backend.airo.persistence.area_code.repository;

import backend.airo.persistence.area_code.entity.CityCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityCodeJpaRepository extends JpaRepository<CityCodeEntity, Long> {

    Optional<CityCodeEntity> findByCtprvnCdAndMegaCodeId(Long ctprvnCd, Long megaCodeId);
    Optional<CityCodeEntity> findByCtprvnNmAndMegaCodeId(String ctprvnNm, Long megaCodeId);


}
