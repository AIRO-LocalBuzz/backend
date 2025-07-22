package backend.airo.domain.point.command;

import backend.airo.domain.point.TradePoint;
import backend.airo.domain.point.repository.TradePointRepository;
import backend.airo.domain.point.vo.TradePointStatus;
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
