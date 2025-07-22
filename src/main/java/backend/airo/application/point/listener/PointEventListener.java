package backend.airo.application.point.listener;

import backend.airo.application.point.usecase.PointUseCase;
import backend.airo.domain.point.event.PointAddedEvent;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointEventListener {

    private final PointUseCase pointUseCase;

    @Description("후기 작성시 사용자에게 포인트를 적립한다.")
    @Async
    @EventListener(PointAddedEvent.class)
    public void onInquiryAddedEvent(PointAddedEvent event) {
        pointUseCase.savePoint(
                event.getUserId(),
                event.getPoint()
        );
    }

}
