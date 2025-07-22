package backend.airo.domain.point.query;

import backend.airo.domain.point.Point;
import backend.airo.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetPointQuery {

    private final PointRepository pointRepository;

    public Long handle(Long userId) {
        return pointRepository.findByUserId(userId)
                .map(Point::getPointScore)
                .orElse(0L);
    }
}
