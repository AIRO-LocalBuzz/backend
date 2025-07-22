package backend.airo.persistence.point.entity;

import backend.airo.domain.point.PointHistory;
import backend.airo.domain.point.vo.PointType;
import backend.airo.persistence.abstracts.ImmutableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_history_points")
public class PointHistoryEntity extends ImmutableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    private Long point;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private PointType type;

    public PointHistoryEntity(Long point, Long userId, PointType type) {
        this.point = point;
        this.userId = userId;
        this.type = type;
    }

    public static PointHistoryEntity toEntity(PointHistory pointHistory) {
        return new PointHistoryEntity(
                pointHistory.getPoint(),
                pointHistory.getUserId(),
                pointHistory.getType()
        );
    }

    public static PointHistory toDomain(PointHistoryEntity pointHistoryEntity) {
        return new PointHistory(
                pointHistoryEntity.id,
                pointHistoryEntity.point,
                pointHistoryEntity.userId,
                pointHistoryEntity.type,
                pointHistoryEntity.getCreatedAt()
        );
    }
}
