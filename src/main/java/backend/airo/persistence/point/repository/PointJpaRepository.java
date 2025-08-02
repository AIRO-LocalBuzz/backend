package backend.airo.persistence.point.repository;

import backend.airo.persistence.point.entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointJpaRepository extends JpaRepository<PointEntity, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        INSERT INTO user_point (user_id, point_score)
        VALUES (:userId, :delta)
        ON DUPLICATE KEY UPDATE point_score = point_score + :delta
        """, nativeQuery = true)
    void upsertIncrement(@Param("userId") Long userId, @Param("delta") long delta);

    PointEntity findByUserId(Long userId);
}
