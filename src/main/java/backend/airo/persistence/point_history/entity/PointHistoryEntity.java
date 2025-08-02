package backend.airo.persistence.point_history.entity;

import backend.airo.domain.point_history.PointHistory;
import backend.airo.domain.point_history.vo.PointType;
import backend.airo.persistence.abstracts.ImmutableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "user_history_points",
        uniqueConstraints = {
                @UniqueConstraint(name = "ux_ph_user_type_source", columnNames = {"user_id", "point_type", "post_id"}),
                @UniqueConstraint(name = "ux_ph_idem_key",      columnNames = {"idempotency_key"})
        },
        indexes = {
                @Index(name = "idx_ph_user_created", columnList = "user_id, created_at"),
                @Index(name = "idx_ph_type_source",  columnList = "point_type, post_id")
        }
)
public class PointHistoryEntity extends ImmutableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "point", nullable = false)
    private Long point;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "point_type", nullable = false, length = 40)
    private PointType type;

    @Column(name = "post_id")
    private Long postId;

    // 멱등키는 항상 세팅 + NOT NULL + UNIQUE
    @Column(name = "idempotency_key", length = 128, nullable = false)
    private String idempotencyKey;

    public PointHistoryEntity(Long point, Long userId, PointType type, Long sourceId, String idempotencyKey) {
        this.point = point;
        this.userId = userId;
        this.type = type;
        this.postId = sourceId;
        this.idempotencyKey = idempotencyKey;
    }

    public static PointHistoryEntity toEntity(PointHistory ph) {
        return new PointHistoryEntity(
                ph.getPoint(),
                ph.getUserId(),
                ph.getType(),
                ph.getPostId(),
                ph.getIdempotencyKey()
        );
    }

    public static PointHistory toDomain(PointHistoryEntity e) {
        return new PointHistory(
                e.id,
                e.point,
                e.userId,
                e.type,
                e.postId,
                e.idempotencyKey,
                e.getCreatedAt()
        );
    }
}