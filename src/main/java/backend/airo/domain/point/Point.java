package backend.airo.domain.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Point {

    private final Long id;
    private final Long userId;
    private Long pointScore = 0L;

    public Point(Long id, Long userId, Long pointScore) {
        this.id = id;
        this.userId = userId;
        this.pointScore = pointScore;
    }

    public void updatePoint(Long point) {
        this.pointScore += point;
    }
}
