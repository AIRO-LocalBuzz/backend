package backend.airo.domain.point_history.query;

import backend.airo.domain.point_history.repository.TradePointRepository;
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
