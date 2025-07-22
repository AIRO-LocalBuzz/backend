package backend.airo.domain.point_history;

import backend.airo.domain.point_history.vo.PointType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PointHistory {

    private final Long id;
    private final Long point;
    private final Long userId;
    private final PointType type;
    private final LocalDateTime createAt;

}
