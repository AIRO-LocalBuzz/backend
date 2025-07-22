package backend.airo.domain.point.query;

import backend.airo.domain.point.Point;
import backend.airo.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetPointListQuery {

    private final PointRepository pointRepository;

    public List<Point> getPointList(Long userId) {
        return pointRepository.getPointListByUserId(userId);
    }

}
