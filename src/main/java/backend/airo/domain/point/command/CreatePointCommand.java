package backend.airo.domain.point.command;

import backend.airo.domain.point.PointHistory;
import backend.airo.domain.point.repository.PointRepository;
import backend.airo.domain.point.vo.PointType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatePointCommand {

    private final PointRepository pointRepository;

    public PointHistory handle(Long userId, Long point) {
        return pointRepository.save(
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
