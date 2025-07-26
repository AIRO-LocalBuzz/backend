package backend.airo.persistence.shop.repository;

import backend.airo.persistence.shop.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopJpaRepository extends JpaRepository<ShopEntity, Long> {

}
