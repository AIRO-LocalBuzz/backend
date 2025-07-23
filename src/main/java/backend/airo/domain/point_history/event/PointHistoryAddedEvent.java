package backend.airo.domain.point_history.event;

import backend.airo.domain.point_history.vo.PointType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PointHistoryAddedEvent {

    private Long point;
    private Long userId;
    private PointType type;

    public PointHistoryAddedEvent(Long point, Long userId, PointType type) {
        this.point = point;
        this.userId = userId;
        this.type = type;
    }
}
