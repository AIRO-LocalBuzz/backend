package backend.airo.domain.point_history.command;

import backend.airo.domain.point_history.PointHistory;
import backend.airo.domain.point_history.repository.PointHistoryRepository;
import backend.airo.domain.point_history.vo.PointType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatePointHistoryCommand {

    private final PointHistoryRepository pointHistoryRepository;

    public PointHistory handle(Long userId, Long point) {
        return pointHistoryRepository.save(
                new PointHistory(
                        0L,
                        point,
                        userId,
                        PointType.REPORT,
                        null
                )
        );
    }
}
