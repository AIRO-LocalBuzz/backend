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
                ps.setString(1, item.getBizKey());
                ps.setString(2, item.getId());
                ps.setString(3, item.getFstvlNm());
                ps.setString(4, item.getOpar());
                ps.setString(5, item.getFstvlCo());
                ps.setObject(6, item.getPeriod().start());
                ps.setObject(7, item.getPeriod().end());
                ps.setDouble(8, item.getLocation().lat());
                ps.setDouble(9, item.getLocation().lon());
                ps.setString(10, item.getAddress().road());
                ps.setString(11, item.getAddress().lot());
                ps.setString(12, item.getAddress().megaCodeId());
                ps.setString(13, item.getAddress().ctprvnCodeId());
                ps.setString(14, item.getMnnstNm());
                ps.setString(15, item.getAuspcInsttNm());
                ps.setString(16, item.getSuprtInsttNm());
                ps.setString(17, item.getPhoneNumber());
                ps.setString(18, item.getHomepageUrl());
                ps.setString(19, item.getRelateInfo());
                ps.setObject(20, item.getReferenceDate());
                ps.setString(21, item.getInsttCode());
                ps.setString(22, item.getInsttNm());
                ps.setObject(23, java.time.LocalDateTime.now());
                ps.setObject(24, java.time.LocalDateTime.now());
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
                biz_key,
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
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                updated_at = CURRENT_TIMESTAMP
        """;
    }
}

