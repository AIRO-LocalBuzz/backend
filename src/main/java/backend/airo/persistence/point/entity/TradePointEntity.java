package backend.airo.persistence.point.entity;

import backend.airo.domain.point.TradePoint;
import backend.airo.domain.point.vo.TradePointStatus;
import backend.airo.persistence.abstracts.ImmutableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_trade_points")
public class TradePointEntity extends ImmutableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    private Long usedPoint;

    private Long userId;

    private String item_name;

    @Enumerated(EnumType.STRING)
    private TradePointStatus tradePointStatus;


    public TradePointEntity(Long usedPoint, Long userId, String item_name, TradePointStatus tradePointStatus) {
        this.usedPoint = usedPoint;
        this.userId = userId;
        this.item_name = item_name;
        this.tradePointStatus = tradePointStatus;
    }

    public static TradePointEntity toEntity(TradePoint tradePoint) {
        return new TradePointEntity(
                tradePoint.getUsedPoint(),
                tradePoint.getUserId(),
                tradePoint.getItem_name(),
                tradePoint.getTradePointStatus()
        );
    }

    public static TradePoint toDomain(TradePointEntity tradePoint) {
        return new TradePoint(
                tradePoint.id,
                tradePoint.usedPoint,
                tradePoint.userId,
                tradePoint.item_name,
                tradePoint.tradePointStatus,
                tradePoint.getCreatedAt()
        );
    }
}
