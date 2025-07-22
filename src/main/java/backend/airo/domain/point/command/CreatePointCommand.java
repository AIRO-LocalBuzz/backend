package backend.airo.domain.point.command;

import backend.airo.domain.point.Point;
import backend.airo.domain.point.repository.PointRepository;
import backend.airo.domain.point.vo.PointType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatePointCommand {

    private final PointRepository pointRepository;

    public Point handle(Long userId, Long point) {
        return pointRepository.save(
                new Point(
                        point,
                        userId,
                        PointType.REPORT
                )
        );
    }
}
