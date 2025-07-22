package backend.airo.application.point.usecase;

import backend.airo.domain.point.Point;
import backend.airo.domain.point.command.CreatePointCommand;
import backend.airo.domain.point.query.GetPointListQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TradedPointUseCase {

    private final CreatePointCommand createPointCommand;
    private final GetPointListQuery getPointListQuery;

    public Point savePoint(Long userId, Long point) {
        return createPointCommand.handle(userId, point);
    }

    public List<Point> getPointList(Long userId) {
        return getPointListQuery.getPointList(userId);
    }

}
