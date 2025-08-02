package backend.airo.application.point.usecase;

import backend.airo.domain.point.query.CountPointQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointUseCase {

    private final CountPointQuery countPointQuery;

    public Long getCountPoint(Long userId) {
        return countPointQuery.handle(userId);
    }

}
