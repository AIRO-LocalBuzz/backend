package backend.airo.domain.point.command;

import backend.airo.domain.point.Point;
import backend.airo.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpsertPointCommand {

    private final PointRepository pointRepository;

    public void handle(Long userId, Long point) {
        pointRepository.upsertIncrement(userId, point);
    }
}
