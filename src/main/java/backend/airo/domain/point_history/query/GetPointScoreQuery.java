package backend.airo.domain.point_history.query;

import backend.airo.domain.point_history.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetPointScoreQuery {

    private final PointHistoryRepository pointHistoryRepository;

    public Long handle(Long userId) {
        return pointHistoryRepository.getPointScoreByUserId(userId);
    }

}
