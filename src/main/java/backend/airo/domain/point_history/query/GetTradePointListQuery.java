package backend.airo.domain.point_history.query;

import backend.airo.domain.point_history.TradePoint;
import backend.airo.domain.point_history.repository.TradePointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetTradePointListQuery {

    private final TradePointRepository tradePointRepository;

    public List<TradePoint> handle(Long userId) {
        return tradePointRepository.getTradePointListByUserId(userId);
    }

}
