package backend.airo.persistence.clutrfatvl.repository;

import backend.airo.persistence.clutrfatvl.entity.ClutrFatvlEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface ClutrFatvlJpaRepository extends JpaRepository<ClutrFatvlEntity, Long> {

    Page<ClutrFatvlEntity> findByAddress_MegaCodeIdAndAddress_CtprvnCodeId(Integer address_megaCodeId, Integer address_ctprvnCodeId, Pageable pageable);


    @NotNull
    @Override
    @EntityGraph(attributePaths = "phoneNumber")
    Optional<ClutrFatvlEntity> findById(@NotNull Long id);
}
