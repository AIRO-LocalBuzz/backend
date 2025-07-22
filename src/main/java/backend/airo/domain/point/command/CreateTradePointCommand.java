package backend.airo.domain.point.command;

import backend.airo.domain.point.TradePoint;
import backend.airo.domain.point.repository.TradePointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateTradePointCommand {

    private final TradePointRepository tradePointRepository;

    public TradePoint handle(Long userId, Long userPoint, String itemName) {
        return tradePointRepository.save(
                new TradePoint(
                        0L,
                        userPoint,
                        userId,
                        itemName,
                        null
                )
        );
    }
}
