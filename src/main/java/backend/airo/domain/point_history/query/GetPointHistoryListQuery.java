package backend.airo.domain.point_history.query;

import backend.airo.domain.point_history.PointHistory;
import backend.airo.domain.point_history.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetPointHistoryListQuery {

    private final PointHistoryRepository pointHistoryRepository;

    public List<PointHistory> handle(Long userId) {
        return pointHistoryRepository.getPointListByUserId(userId);
    }

}
