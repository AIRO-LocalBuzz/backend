package backend.airo.domain.point;

import backend.airo.domain.point.vo.PointType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Point {

    private Long id = 0L;
    private Long point;
    private Long userId;
    private PointType type;

    public Point(Long id, Long point, Long userId, PointType type) {
        this.id = id;
        this.point = point;
        this.userId = userId;
        this.type = type;
    }

    public Point(Long point, Long userId, PointType type) {
        this.point = point;
        this.userId = userId;
        this.type = type;
    }
}
