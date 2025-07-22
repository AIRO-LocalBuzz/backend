package backend.airo.domain.point.command;

import backend.airo.domain.point.Point;
import backend.airo.domain.point.repository.PointRepository;
import backend.airo.domain.point_history.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdatePointCommand {

    private final PointRepository pointRepository;

    public Point handle(Long userId, Long point) {
        Point findPint = pointRepository.findById(userId);
        findPint.updatePoint(point);
        return pointRepository.save(findPint);
    }
}
