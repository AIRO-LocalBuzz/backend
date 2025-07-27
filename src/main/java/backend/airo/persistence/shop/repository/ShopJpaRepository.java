package backend.airo.persistence.shop.repository;

import backend.airo.persistence.shop.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopJpaRepository extends JpaRepository<ShopEntity, Long> {

    //도시 이름, 지역구 이름을 통한 필터링
    @Query(value = """
            SELECT s.*
            FROM shop_entity s
            JOIN city_code_entity c ON s.signgu_cd = c.ctprvn_cd
            JOIN mega_code_entity m ON s.ctprvn_cd = m.ctprvn_cd
            WHERE m.ctprvn_nm = :megaName AND c.ctprvn_nm = :cityName
            """, nativeQuery = true)
    List<ShopEntity> findByShopList(String megaName, String cityName);

}
