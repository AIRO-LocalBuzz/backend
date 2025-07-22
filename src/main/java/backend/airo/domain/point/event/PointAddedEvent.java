package backend.airo.domain.point.event;

import backend.airo.domain.point.vo.PointType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PointAddedEvent {

    private Long point;
    private Long userId;
    private PointType type;

    public PointAddedEvent(Long point, Long userId, PointType type) {
        this.point = point;
        this.userId = userId;
        this.type = type;
    }
}
