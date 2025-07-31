package backend.airo.persistence.shop.repository;

import backend.airo.persistence.shop.entity.ShopEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopJpaRepository extends JpaRepository<ShopEntity, Long> {

    Page<ShopEntity> findByRegion_CtprvnCdAndRegion_SignguCd(String regionCtprvnCd, String regionSignguCd, Pageable pageable);

}
