package backend.airo.domain.point.query;

import backend.airo.domain.point.repository.PointRepository;
import backend.airo.domain.point.repository.TradePointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetTradePointScoreQuery {

    private final TradePointRepository tradePointRepository;

    public Long handle(Long userId) {
        return tradePointRepository.getTradePointScore(userId);
    }

}
