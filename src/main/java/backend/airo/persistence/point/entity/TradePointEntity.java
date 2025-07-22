package backend.airo.persistence.point.entity;

import backend.airo.domain.point.TradePoint;
import backend.airo.persistence.abstracts.ImmutableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_points")
public class TradePointEntity extends ImmutableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    private Long useedPoint;

    private Long userId;

    private String item_name;


    public TradePointEntity(Long point, Long userId, String itemName) {
        this.useedPoint = point;
        this.userId = userId;
        this.item_name = itemName;
    }

    public static TradePointEntity toEntity(TradePoint tradePoint) {
        return new TradePointEntity(
                tradePoint.getUseedPoint(),
                tradePoint.getUserId(),
                tradePoint.getItem_name()
        );
    }

    public static TradePoint toDomain(TradePointEntity tradePoint) {
        return new TradePoint(
                tradePoint.id,
                tradePoint.useedPoint,
                tradePoint.userId,
                tradePoint.item_name,
                tradePoint.getCreatedAt()
        );
    }
}
