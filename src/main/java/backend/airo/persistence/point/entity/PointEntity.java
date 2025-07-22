package backend.airo.persistence.point.entity;

import backend.airo.domain.point.Point;
import backend.airo.domain.point.vo.PointType;
import backend.airo.persistence.abstracts.ImmutableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_points")
public class PointEntity extends ImmutableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    private Long point;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private PointType type;

    public PointEntity(Long point, Long userId, PointType type) {
        this.point = point;
        this.userId = userId;
        this.type = type;
    }

    public static PointEntity toEntity(Point point) {
        return new PointEntity(
                point.getPoint(),
                point.getUserId(),
                point.getType()
        );
    }

    public static Point toDomain(PointEntity pointEntity) {
        return new Point(
                pointEntity.id,
                pointEntity.point,
                pointEntity.userId,
                pointEntity.type,
                pointEntity.getCreatedAt()
        );
    }
}
