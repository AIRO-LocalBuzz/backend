package backend.airo.persistence.clutrfatvl.repository;

import backend.airo.persistence.clutrfatvl.entity.ClutrFatvlEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
public interface ClutrFatvlJpaRepository extends JpaRepository<ClutrFatvlEntity, Long> {

    Page<ClutrFatvlEntity> findByAddress_MegaCodeIdAndAddress_CtprvnCodeId(String regionCtprvnCd, String regionSignguCd, Pageable pageable);

    @Modifying
    @Query("DELETE FROM ClutrFatvlEntity f WHERE f.period.start BETWEEN :start AND :end")
    void deleteAllByFstvlStartDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);



}
