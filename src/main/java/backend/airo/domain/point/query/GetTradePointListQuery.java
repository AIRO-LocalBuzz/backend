package backend.airo.domain.point.query;

import backend.airo.domain.point.TradePoint;
import backend.airo.domain.point.repository.TradePointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetTradePointListQuery {

    private final TradePointRepository tradePointRepository;

    public List<TradePoint> getTradePointList(Long userId) {
        return tradePointRepository.getPointListByUserId(userId);
    }

}
