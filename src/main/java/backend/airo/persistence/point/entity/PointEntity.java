package backend.airo.persistence.point.entity;

import backend.airo.domain.point.Point;
import backend.airo.persistence.abstracts.ImmutableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_point")
public class PointEntity extends ImmutableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    private Long userId;

    private Long pointScore;

    public PointEntity(Long userId, Long pointScore) {
        this.userId = userId;
        this.pointScore = pointScore;
    }

    public void updatePoint(Point point) {
        this.pointScore = point.getPointScore();
    }

    public static PointEntity toEntity(Point point) {
        return new PointEntity(
                point.getUserId(),
                point.getPointScore()
        );
    }

    public static Point toDomain(PointEntity pointEntity) {
        return new Point(
                pointEntity.id,
                pointEntity.userId,
                pointEntity.pointScore
        );
    }
}
