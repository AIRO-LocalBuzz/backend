package backend.airo.persistence.clutrfatvl.repository;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.domain.clure_fatvl.repository.ClutrFatvlBulkRepositoryPort;
import backend.airo.persistence.clutrfatvl.entity.ClutrFatvlEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ClutrFatvlBulkRepository implements ClutrFatvlBulkRepositoryPort {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void batchInsert(List<ClutrFatvl> items) {
        List<ClutrFatvlEntity> clutrFatvlEntities = items.stream().map(ClutrFatvlEntity::toEntity).toList();
        int[] results = jdbcTemplate.batchUpdate(makeBulkSql(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                ClutrFatvlEntity item = clutrFatvlEntities.get(i);
                ps.setString(1, item.getId());
                ps.setString(2, item.getFstvlNm());
                ps.setString(3, item.getOpar());
                ps.setString(4, item.getFstvlCo());
                ps.setObject(5, item.getPeriod().start());
                ps.setObject(6, item.getPeriod().end());
                ps.setDouble(7, item.getLocation().lat());
                ps.setDouble(8, item.getLocation().lon());
                ps.setString(9, item.getAddress().road());
                ps.setString(10, item.getAddress().lot());
                ps.setString(11, item.getAddress().megaCodeId());
                ps.setString(12, item.getAddress().ctprvnCodeId());
                ps.setString(13, item.getMnnstNm());
                ps.setString(14, item.getAuspcInsttNm());
                ps.setString(15, item.getSuprtInsttNm());
                ps.setString(16, item.getPhoneNumber());
                ps.setString(17, item.getHomepageUrl());
                ps.setString(18, item.getRelateInfo());
                ps.setObject(19, item.getReferenceDate());
                ps.setString(20, item.getInsttCode());
                ps.setString(21, item.getInsttNm());
                ps.setObject(22, java.time.LocalDateTime.now());
                ps.setObject(23, java.time.LocalDateTime.now());
            }

            @Override
            public int getBatchSize() {
                return items.size();
            }
        });
        int totalInserted = Arrays.stream(results).sum();
        log.info("[batchInsert] batch size: {}, inserted count: {}", clutrFatvlEntities.size(), totalInserted);
    }

    private static String makeBulkSql() {
        return """
            INSERT INTO clutr_fatvl (
                id, fstvl_nm, opar, fstvl_co,
                start_date, end_date,
                latitude, longitude,
                road_addr, lot_addr,
                mega_code_id, ctprvn_code_id,
                mnnst_nm, auspc_instt_nm, suprt_instt_nm,
                phone_number, homepage_url, relate_info,
                reference_date, instt_code, instt_nm,
                created_at, updated_at
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
    }
}

