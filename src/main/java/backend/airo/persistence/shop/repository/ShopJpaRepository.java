package backend.airo.persistence.shop.repository;

import backend.airo.persistence.shop.entity.ShopEntity;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShopJpaRepository extends JpaRepository<ShopEntity, Long> {

    //TODO QueryDSL을 사용하여 성능 개선하기
    @Query("""
            SELECT s
            FROM ShopEntity s
            WHERE s.region.ctprvnCd = :ctprvn
              AND s.region.signguCd = :signgu
              AND s.industry.indsLclsCd = :largeCategoryCode
              AND ( :middleCategoryCode IS NULL OR s.industry.indsMclsCd = :middleCategoryCode )
              AND ( :smallCategoryCode  IS NULL OR s.industry.indsSclsCd = :smallCategoryCode )
            """)
    Page<ShopEntity> searchByRegionAndOptionalCategories(
            @Param("ctprvn") String ctprvn,
            @Param("signgu") String signgu,
            @Param("largeCategoryCode") String largeCategoryCode,
            @Param("middleCategoryCode") String middleCategoryCode, // null이면 무시
            @Param("smallCategoryCode") String smallCategoryCode,  // null이면 무시
            Pageable pageable
    );
}
