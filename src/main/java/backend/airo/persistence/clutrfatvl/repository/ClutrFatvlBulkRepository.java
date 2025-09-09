package backend.airo.persistence.clutrfatvl.repository;

import backend.airo.domain.clure_fatvl.ClutrFatvl;
import backend.airo.domain.clure_fatvl.repository.ClutrFatvlBulkRepositoryPort;
import backend.airo.domain.clure_fatvl.vo.ClutrFatvlPhoneNumber;
import backend.airo.persistence.clutrfatvl.entity.ClutrFatvlEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
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
        int[] results = jdbcTemplate.batchUpdate(makeClutrFatvlBulkSql(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                ClutrFatvlEntity item = clutrFatvlEntities.get(i);
                ps.setString(1, String.valueOf(item.getContentId()));
                ps.setString(2, String.valueOf(item.getContenttypeId()));
                ps.setString(3, item.getTitle());
                ps.setString(4, String.valueOf(item.getLocation().lat()));
                ps.setString(5, String.valueOf(item.getLocation().lon()));
                ps.setObject(6, item.getAddress().addr1());
                ps.setObject(7, item.getAddress().addr2());
                ps.setDouble(8, item.getAddress().megaCodeId());
                ps.setDouble(9, item.getAddress().ctprvnCodeId());
                ps.setString(10, item.getCat1());
                ps.setString(11, item.getFirstImage());
                ps.setString(12, item.getFirstImage2());
                ps.setString(13, String.valueOf(item.getModifiedDate()));
            }

            @Override
            public int getBatchSize() {
                return items.size();
            }


        });
        int totalInserted = Arrays.stream(results).sum();
        log.info("[batchInsert] batch size: {}, inserted count: {}", clutrFatvlEntities.size(), totalInserted);

        List<ClutrFatvlPhoneNumber> clutrFatvlPhoneNumbers = new ArrayList<>();

        for (ClutrFatvlEntity clutrFatvlEntity : clutrFatvlEntities) {
            List<String> phoneNumbers = clutrFatvlEntity.getPhoneNumber();
            for (int i = 1; i <= phoneNumbers.size(); i++) {
                clutrFatvlPhoneNumbers.add(new ClutrFatvlPhoneNumber(clutrFatvlEntity.getContentId(), i, phoneNumbers.get(i-1)));
            }
        }

        if (!clutrFatvlPhoneNumbers.isEmpty()) {
            jdbcTemplate.batchUpdate(makeClutrFatvlPhoneBulkSql(), new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ClutrFatvlPhoneNumber r = clutrFatvlPhoneNumbers.get(i);
                            ps.setLong(1, r.contentId());
                            ps.setInt(2, r.seq());
                            ps.setString(3, r.phoneNumber());
                        }
                        @Override
                        public int getBatchSize() {
                            return clutrFatvlPhoneNumbers.size();
                        }
                    }
            );
        }
    }


    private static String makeClutrFatvlBulkSql() {
        return """
                    INSERT INTO clutr_fatvl (
                        content_id,
                        contenttype_id,
                        title,
                        latitude,
                        longitude,
                        addr1,
                        addr2,
                        mega_code_id,
                        ctprvn_code_id,
                        cat1,
                        first_image,
                        first_image2,
                        modified_date
                    )
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                      title          = IF(VALUES(modified_date) > modified_date OR modified_date IS NULL, VALUES(title), title),
                      latitude       = IF(VALUES(modified_date) > modified_date OR modified_date IS NULL, VALUES(latitude), latitude),
                      longitude      = IF(VALUES(modified_date) > modified_date OR modified_date IS NULL, VALUES(longitude), longitude),
                      addr1          = IF(VALUES(modified_date) > modified_date OR modified_date IS NULL, VALUES(addr1), addr1),
                      addr2          = IF(VALUES(modified_date) > modified_date OR modified_date IS NULL, VALUES(addr2), addr2),
                      mega_code_id   = IF(VALUES(modified_date) > modified_date OR modified_date IS NULL, VALUES(mega_code_id), mega_code_id),
                      ctprvn_code_id = IF(VALUES(modified_date) > modified_date OR modified_date IS NULL, VALUES(ctprvn_code_id), ctprvn_code_id),
                      cat1           = IF(VALUES(modified_date) > modified_date OR modified_date IS NULL, VALUES(cat1), cat1),
                      first_image    = IF(VALUES(modified_date) > modified_date OR modified_date IS NULL, VALUES(first_image), first_image),
                      first_image2   = IF(VALUES(modified_date) > modified_date OR modified_date IS NULL, VALUES(first_image2), first_image2),
                      modified_date  = IF(VALUES(modified_date) > modified_date OR modified_date IS NULL, VALUES(modified_date), modified_date);
                """;
    }

    private static String makeClutrFatvlPhoneBulkSql() {
        return """
                    INSERT INTO clutr_fatvl_phone (
                        content_id,
                        seq,
                        phone_number
                    ) VALUES (?, ?, ?)
                    ON DUPLICATE KEY UPDATE phone_number = VALUES(phone_number)
                """;
    }
}

