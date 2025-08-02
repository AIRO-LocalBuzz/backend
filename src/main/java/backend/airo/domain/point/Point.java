package backend.airo.domain.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Point {

    private final Long userId;
    private Long pointScore = 0L;

    public Point(Long userId, Long pointScore) {
        this.userId = userId;
        this.pointScore = pointScore;
    }

}
