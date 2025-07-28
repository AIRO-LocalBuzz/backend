package backend.airo.persistence.shop.repository;

import backend.airo.persistence.shop.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopJpaRepository extends JpaRepository<ShopEntity, Long> {

    List<ShopEntity> findByRegion_CtprvnCdAndRegion_SignguCd(String regionCtprvnCd, String regionSignguCd);

}
