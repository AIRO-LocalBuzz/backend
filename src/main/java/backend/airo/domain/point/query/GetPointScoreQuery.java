package backend.airo.domain.point.query;

import backend.airo.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetPointScoreQuery {

    private final PointRepository pointRepository;

    public Long handle(Long userId) {
        return pointRepository.getPointScoreByUserId(userId);
    }

}
