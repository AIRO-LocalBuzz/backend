package backend.airo.domain.point_history.command;

import backend.airo.domain.point_history.TradePoint;
import backend.airo.domain.point_history.repository.TradePointRepository;
import backend.airo.domain.point_history.vo.TradePointStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateTradePointCommand {

    private final TradePointRepository tradePointRepository;

    public TradePoint handle(Long userId, Long userPoint, String itemName, TradePointStatus tradePointStatus) {
        return tradePointRepository.save(
                new TradePoint(
                        0L,
                        userPoint,
                        userId,
                        itemName,
                        tradePointStatus,
                        null
                )
        );
    }
}
